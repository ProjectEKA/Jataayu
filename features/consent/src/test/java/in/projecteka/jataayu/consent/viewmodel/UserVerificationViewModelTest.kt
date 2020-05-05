package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
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
    private lateinit var credRepo: CredentialsRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var call: Call<UserVerificationResponse>

    private lateinit var userVerificationViewModel: UserVerificationViewModel

    private lateinit var userVerificationRequestForGrand: UserVerificationRequest

    private lateinit var userVerificationRequestForRevoke: UserVerificationRequest

    private lateinit var userVerificationResponse: UserVerificationResponse

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userVerificationViewModel = UserVerificationViewModel(userVerificationRepository, credRepo, preferenceRepository)
        userVerificationRequestForGrand = Gson().fromJson<UserVerificationRequest>("{\"pin\":\"1234\",\"scope\":\"consentrequest.approve\"}")
        userVerificationRequestForRevoke = Gson().fromJson<UserVerificationRequest>("{\"pin\":\"1234\",\"scope\":\"consent.revoke\"}")
        userVerificationResponse = Gson().fromJson<UserVerificationResponse>("{\"temporaryToken\":\"12345abc\"}")
    }

    @Test
    fun `should call verify user method of repository for grand consent`() {
        `when`(userVerificationRepository.verifyUser(userVerificationRequestForGrand)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<UserVerificationResponse>
                callback.onResponse(call, Response.success(userVerificationResponse))
            }


        userVerificationViewModel.verifyUser("1234", ConsentScopeType.SCOPE_GRAND)
        Mockito.verify(userVerificationRepository).verifyUser(userVerificationRequestForGrand)
        Mockito.verify(call).enqueue(any())
    }

    @Test
    fun `should call verify user method of repository for revoke consent`() {
        `when`(userVerificationRepository.verifyUser(userVerificationRequestForRevoke)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<UserVerificationResponse>
                callback.onResponse(call, Response.success(userVerificationResponse))
            }


        userVerificationViewModel.verifyUser("1234", ConsentScopeType.SCOPE_REVOKE)
        Mockito.verify(userVerificationRepository).verifyUser(userVerificationRequestForRevoke)
        Mockito.verify(call).enqueue(any())
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(userVerificationRepository)
        Mockito.validateMockitoUsage()
    }
}