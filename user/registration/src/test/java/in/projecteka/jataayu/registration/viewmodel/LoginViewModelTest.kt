package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.registration.model.LoginRequest
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AuthenticationRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    @Mock
    private lateinit var call: Call<CreateAccountResponse>

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var loginResponse: CreateAccountResponse

    private lateinit var loginRequest: LoginRequest


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        loginViewModel = LoginViewModel(repository,preferenceRepository,credentialsRepository)

        loginResponse = Gson()
            .fromJson(TestUtils.readFile("login_response.json"), CreateAccountResponse::class.java)

    }

    @Test
    fun shouldCallRepositoryLogin() {

        val username = "username"
        val provider = "@ncg"
        val password = "password@135"
        loginViewModel.usernameProviderLbl.set(provider)
        loginViewModel.inputPasswordLbl.set(password)
        loginViewModel.inputUsernameLbl.set(username)

        Mockito.`when`(repository.login(username + provider,password, "password")).thenReturn(call)
        Mockito.`when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
                callback.onResponse(call, Response.success(loginResponse))
            }


        loginViewModel.onLoginClicked()
        verify(repository).login(username + provider, password, "password")
        verify(call).enqueue(any())
    }
}
