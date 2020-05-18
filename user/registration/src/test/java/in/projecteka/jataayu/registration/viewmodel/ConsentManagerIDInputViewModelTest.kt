package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.LoginMode
import `in`.projecteka.jataayu.core.model.LoginType
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.model.APIResponse
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
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsentManagerIDInputViewModelTest {

    private lateinit var consentManagerIDInputViewModel: ConsentManagerIDInputViewModel
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var registerEventObserver: Observer<Void>

    @Mock
    private lateinit var nextButtonEventObserver: Observer<Void>

    @Mock
    private lateinit var forgotPasswordObserver: Observer<Void>

    @Mock
    private lateinit var userAccountRepository: UserAccountsRepository

    @Mock
    private lateinit var loginModeResponseCall: Call<LoginType>

    @Mock
    private lateinit var loginModeResponseObserver: Observer<APIResponse<out LoginMode>?>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        consentManagerIDInputViewModel = ConsentManagerIDInputViewModel(userAccountRepository)
        loginViewModel = LoginViewModel()
        consentManagerIDInputViewModel.onRegisterButtonClickEvent.observeForever(
            registerEventObserver
        )
        consentManagerIDInputViewModel.onNextButtonClickEvent.observeForever(nextButtonEventObserver)
        consentManagerIDInputViewModel.onForgetCMIDButtonClickEvent.observeForever(
            forgotPasswordObserver
        )
        consentManagerIDInputViewModel.loginMode.observeForever(loginModeResponseObserver)


    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(registerEventObserver)
    }

    @Test
    fun `should trigger register event when register button clicked`() {
        consentManagerIDInputViewModel.onRegisterButtonClicked()
        verify(registerEventObserver, times(1)).onChanged(null)
    }

    @Test
    fun `should trigger next event event when next button clicked`() {
        consentManagerIDInputViewModel.onNextButtonClicked()
        verify(nextButtonEventObserver, times(1)).onChanged(null)
    }

    @Test
    fun `should trigger forgot cm id event event when forgot cm id button clicked`() {
        consentManagerIDInputViewModel.onForgotConsentManagerID()
        verify(forgotPasswordObserver, times(1)).onChanged(null)
    }


    @Test
    fun `should next enabled default value must be false`() {
        assertFalse(consentManagerIDInputViewModel.nextEnabled.get())
    }

    @Test
    fun `should disable next button when cm id is empty`() {
        consentManagerIDInputViewModel.inputUsernameLbl.set("")
        assertFalse(consentManagerIDInputViewModel.nextEnabled.get())
    }

    @Test
    fun `should enable next button when cm id is not empty`() {
        consentManagerIDInputViewModel.inputUsernameLbl.set("ab")
        consentManagerIDInputViewModel.onTextChanged("ab", 1, 1, 2)
        assertTrue(consentManagerIDInputViewModel.nextEnabled.get())
    }

    @Test
    fun `should parse login mode response when mode is password`() {

        val loginModeJsonResponse = """{"loginMode": "CREDENTIAL"}"""
        val passwordLoginMode = Gson().fromJson(loginModeJsonResponse, LoginType::class.java)
        assertEquals(LoginMode.PASSWORD, passwordLoginMode.loginMode)
    }

    @Test
    fun `should parse login mode response when mode is otp`() {

        val loginModeJsonResponse = """{"loginMode": "OTP"}"""
        val otpLoginMode = Gson().fromJson(loginModeJsonResponse, LoginType::class.java)
        assertEquals(LoginMode.OTP, otpLoginMode.loginMode)
    }

    @Test
    fun `should fetch login mode when next button pressed`() {

        val loginModeJsonResponse = """{"loginMode": "CREDENTIAL"}"""
        val passwordLoginMode = Gson().fromJson(loginModeJsonResponse, LoginType::class.java)

        val cmId = "vk2704201"
        `when`(userAccountRepository.getLoginMode(cmId)).thenReturn(loginModeResponseCall)
        `when`(loginModeResponseCall.enqueue(any())).then {
            val callback = it.arguments[0] as Callback<LoginType>
            callback.onResponse(loginModeResponseCall, Response.success(passwordLoginMode))
        }

        consentManagerIDInputViewModel.fetchLoginMode(cmId)
        verify(userAccountRepository).getLoginMode(cmId)
        verify(loginModeResponseCall).enqueue(ArgumentMatchers.any())
        verify(loginModeResponseObserver, times(2)).onChanged(null)
        verify(loginModeResponseObserver, times(1)).onChanged(APIResponse(passwordLoginMode.loginMode, null))

    }

}