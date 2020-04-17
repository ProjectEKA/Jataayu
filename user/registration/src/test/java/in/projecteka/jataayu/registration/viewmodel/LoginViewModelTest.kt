package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.registration.model.LoginRequest
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.TestUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
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
    private lateinit var call: Call<CreateAccountResponse>

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var loginResponse: CreateAccountResponse

    private lateinit var loginRequest: LoginRequest


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        loginViewModel = LoginViewModel(repository)

        loginResponse = Gson()
            .fromJson(TestUtils.readFile("login_response.json"), CreateAccountResponse::class.java)

        loginRequest = Gson().fromJson(TestUtils.readFile("login_request.json"), LoginRequest::class.java)

        `when`(repository.login(loginRequest)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
                callback.onResponse(call, Response.success(loginResponse))
            }
    }

    @Test
    fun shouldReturnAuthTokenWithTokenType(){
        assertEquals("Bearer eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI",
            loginViewModel.getAuthTokenWithTokenType("eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI", "Bearer"))
    }

    @Test
    fun shouldCallRepositoryLogin() {
//        loginViewModel.login("username", "password@123")
        verify(repository).login(loginRequest)
        verify(call).enqueue(any())
    }
}