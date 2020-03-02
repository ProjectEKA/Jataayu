package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.util.extension.fromJson
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UserVerificationViewModelTest {
    @Mock
    private lateinit var userVerificationRepository: UserVerificationRepository

    @Mock
    private lateinit var call : Call<UserVerificationResponse>

    @Mock
    private lateinit var responseCallback: ResponseCallback

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldCallRepositoryUserVerificationMethod() {
        val mockResponse = Gson().fromJson<UserVerificationResponse>("{\"isValid\":\"true\"}")
        `when`(userVerificationRepository.verifyUser(anyString())).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<UserVerificationResponse>
                callback.onResponse(call, Response.success(mockResponse))
            }
        UserVerificationViewModel(userVerificationRepository).verifyUser("1234", responseCallback)
        Mockito.verify(userVerificationRepository, times(1)).verifyUser("1234")
        Mockito.verify(responseCallback).onSuccess(any(UserVerificationResponse::class.java))
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(userVerificationRepository)
        Mockito.validateMockitoUsage()
    }
}