package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
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

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userVerificationRepository: UserVerificationRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var credRepo: CredentialsRepository

    @Mock
    private lateinit var call: Call<UserVerificationResponse>

    private lateinit var userVerificationViewModel: UserVerificationViewModel

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userVerificationViewModel = UserVerificationViewModel(userVerificationRepository, credRepo, preferenceRepository)

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
        userVerificationViewModel.verifyUser("1234")
        Mockito.verify(userVerificationRepository, times(1)).verifyUser("1234")
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(userVerificationRepository)
        Mockito.validateMockitoUsage()
    }
}