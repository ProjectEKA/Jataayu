package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private lateinit var redirectEventObserver: Observer<Int>

    @Mock
    private lateinit var loginSuccessResponse: Observer<Void>



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        loginViewModel = LoginViewModel()

        loginViewModel.redirectLiveEvent.observeForever(redirectEventObserver)
        loginViewModel.loginResponseSuccessEvent.observeForever(loginSuccessResponse)
    }

    @Test
    fun shouldCallRepositoryLogin() {
//
//        val username = "username"
//        val provider = "@ncg"
//        val password = "password@135"
//        loginViewModel.usernameProviderLbl.set(provider)
//        loginViewModel.inputPasswordLbl.set(password)
//        loginViewModel.inputUsernameLbl.set(username)
//
//        Mockito.`when`(repository.login(username + provider,password, "password")).thenReturn(call)
//        Mockito.`when`(call.enqueue(any()))
//            .then { invocation ->
//                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
//                callback.onResponse(call, Response.success(loginResponse))
//            }
//
//
//        loginViewModel.onLoginClicked()
//        verify(repository).login(username + provider, password, "password")
//        verify(call).enqueue(any())
    }

    @Test
    fun shouldReturnAccountLock(){
//        val username = "username"
//        val provider = "@ncg"
//        val password = "pass@135"
//        loginViewModel.usernameProviderLbl.set(provider)
//        loginViewModel.inputPasswordLbl.set(password)
//        loginViewModel.inputUsernameLbl.set(username)
//        val error = Error(1031, "User Blocked temporarily")
//        val errorResponse = ErrorResponse(error)
//        val body =  ResponseBody.Companion.create("application/json; charset=utf-8".toMediaTypeOrNull(),Gson().toJson(errorResponse))
//
//        Mockito.`when`(repository.login(username + provider,password, "password")).thenReturn(call)
//        Mockito.`when`(call.enqueue(any()))
//            .then { invocation ->
//                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
//                callback.onResponse(call, Response.error(401,body))
//            }
//
//        loginViewModel.onLoginClicked()
//        verify(repository).login(username + provider, password, "password")
//        verify(call).enqueue(any())
//        verify(loginFetchObserver, Mockito.times(1)).onChanged(Loading(true))
//        verify(loginFetchObserver, Mockito.times(1)).onChanged(Loading(false))
//        verify(loginFetchObserver, Mockito.times(3)).onChanged(PartialFailure(any()))
    }


    @Test
    fun `should update consent manager id`() {
        loginViewModel.updateConsentManagerID("maha", "@ncg")
        assertEquals("maha@ncg", loginViewModel.cmId)
    }

    @Test
    fun `should trigger to redirect even when current fragment is replaced by password fragment`() {
        loginViewModel.replaceFragment(R.layout.password_input_fragment)
        verify(redirectEventObserver).onChanged(R.layout.password_input_fragment)
    }


    @Test
    fun `should trigger login response event when response updated`() {
        loginViewModel.loginResponseSuccessEvent.call()
        verify(loginSuccessResponse, times(1)).onChanged(null)
    }
}
