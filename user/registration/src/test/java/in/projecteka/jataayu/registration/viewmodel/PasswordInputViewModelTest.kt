package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.model.APIResponse
import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.ui.activity.LoginActivity
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.content.res.Resources
import android.text.InputType
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordInputViewModelTest {

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository
    @Mock
    private lateinit var preferenceRepository: PreferenceRepository
    @Mock
    private lateinit var authenticationRepository: AuthenticationRepository
    @Mock
    private lateinit var loginResponseObserver: Observer<APIResponse<out CreateAccountResponse>?>
    @Mock
    private lateinit var forgotPasswordClickObserver: Observer<Void>
    @Mock
    private lateinit var errorDialogObserver: Observer<String>

    @Mock
    private lateinit var onPasswordVisibilityToggleEventObserver: Observer<Int>
    @Mock
    private lateinit var loginCall: Call<CreateAccountResponse>
    @Mock
    private lateinit var resources: Resources

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var passwordInputViewModel: PasswordInputViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginResponse: CreateAccountResponse

    private val visiblePasswordInputType: Int
        get() = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    private val hiddenPasswordInputType: Int
        get() = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD

    private val cmId =  "vk2704201@ncg"
    private val password = "Vik@2704"
    private val grantType = "password"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        passwordInputViewModel = PasswordInputViewModel(
            credentialsRepository,
            preferenceRepository,
            authenticationRepository
        )
        loginViewModel = LoginViewModel()
        loginResponse = Gson()
            .fromJson(TestUtils.readFile("login_response.json"), CreateAccountResponse::class.java)
        passwordInputViewModel.createAccountResponse.observeForever(loginResponseObserver)
        passwordInputViewModel.onClickForgotPasswordEvent.observeForever(forgotPasswordClickObserver)
        passwordInputViewModel.errorDialogEvent.observeForever(errorDialogObserver)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            credentialsRepository,
            preferenceRepository,
            authenticationRepository,
            forgotPasswordClickObserver,
            loginResponseObserver,
            onPasswordVisibilityToggleEventObserver
        )
    }

    @Test
    fun `should disable the login button by default`() {
        assertFalse(passwordInputViewModel.loginEnabled.get())
    }

    @Test
    fun `should enable the login button if password is not empty`() {
        passwordInputViewModel.inputPasswordLbl.set("abc")
        passwordInputViewModel.afterTextChanged(null)
        assertTrue(passwordInputViewModel.loginEnabled.get())
    }

    @Test
    fun `should trigger forgot password event on click of forgot password button`() {
        passwordInputViewModel.onForgotPasswordClicked()
        verify(forgotPasswordClickObserver, times(1)).onChanged(null)
    }

    @Test
    fun `should login api call success`() {

        `when`(authenticationRepository.login(cmId, password, grantType)).thenReturn(loginCall)
        `when`(loginCall.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
                callback.onResponse(loginCall, Response.success(loginResponse))
            }
        passwordInputViewModel.inputPasswordLbl.set("Vik@2704")
        passwordInputViewModel.onLoginClicked("vk2704201@ncg")
        verify(authenticationRepository).login(cmId, password, grantType)
        verify(loginCall).enqueue(ArgumentMatchers.any())
        verify(loginResponseObserver, times(2)).onChanged(null)
        verify(loginResponseObserver, times(1)).onChanged(APIResponse(loginResponse, null))
    }

    @Test
    fun `should update shared preferences on login success`() {

        val accessToken = "${loginResponse.tokenType.capitalize()} ${loginResponse.accessToken}"
        `when`(credentialsRepository.refreshToken).thenReturn(loginResponse.refreshToken)
        `when`(credentialsRepository.accessToken).thenReturn(accessToken)
        `when`(preferenceRepository.isUserLoggedIn).thenReturn(true)

        passwordInputViewModel.onLoginSuccess(loginResponse)

        verify(credentialsRepository).accessToken = accessToken
        verify(credentialsRepository).refreshToken = loginResponse.refreshToken
        verify(preferenceRepository).isUserLoggedIn = true
    }


    @Test
    fun `should trigger error dialog on login failure has unhandled error`() {
        `when`(resources.getString(R.string.something_went_wrong)).thenReturn("Something went wrong")
        val error = Error(-1, "")
        passwordInputViewModel.onLoginFailure(error, resources)
        verify(resources).getString(R.string.something_went_wrong)
        verify(errorDialogObserver).onChanged(resources.getString(R.string.something_went_wrong))
    }

    @Test
    fun `should trigger error dialog on login failure has ubable to parse error response`() {
        `when`(resources.getString(R.string.something_went_wrong)).thenReturn("Something went wrong")
        passwordInputViewModel.onLoginFailure(null, resources)
        verify(resources).getString(R.string.something_went_wrong)
        verify(errorDialogObserver).onChanged(resources.getString(R.string.something_went_wrong))
    }

    @Test
    fun `should display lock account block when user enters the wrong password 5 times`() {
        `when`(resources.getString(R.string.something_went_wrong)).thenReturn("Something went wrong")
        val error = Error(LoginActivity.ERROR_CODE_BLOCK_USER, "")
        passwordInputViewModel.onLoginFailure(error, resources)
        assertEquals(View.VISIBLE, passwordInputViewModel.accountLockBlockDividerEnable.get())
        assertEquals(View.VISIBLE, passwordInputViewModel.accountLockBlockEnable.get())
    }

}