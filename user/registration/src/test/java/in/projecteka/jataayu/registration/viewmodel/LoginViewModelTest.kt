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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call

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

        loginRequest =  Gson().fromJson(TestUtils.readFile("login_request.json"), LoginRequest::class.java)

        //TODO: Modify after refactor
//        `when`(repository.login("username", "password@135", "password")).thenReturn(call)
//        `when`(call.enqueue(any()))
//            .then { invocation ->
//                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
//                callback.onResponse(call, Response.success(loginResponse))
//            }
    }

    //TODO: Modify after refactor
    @Test
    fun shouldCallRepositoryLogin() {
//        verify(repository).login("username", "password@135", "password")
//        verify(call).enqueue(any())
    }
}