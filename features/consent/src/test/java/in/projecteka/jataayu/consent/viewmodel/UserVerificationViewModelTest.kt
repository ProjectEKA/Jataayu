package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.BuildConfig
import `in`.projecteka.jataayu.network.NetworkManager
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.util.constant.NetworkConstants
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.UUIDRepository
import `in`.projecteka.jataayu.util.sharedPref.NETWORK_HOST
import `in`.projecteka.jataayu.util.sharedPref.NETWORK_PREF
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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
    private lateinit var uuidRepository: UUIDRepository

    @Mock
    private lateinit var call: Call<UserVerificationResponse>

    @Mock
    private lateinit var UserVerificationFetchObserver: Observer<PayloadResource<UserVerificationResponse>>

    private lateinit var userVerificationViewModel: UserVerificationViewModel

    private lateinit var userVerificationRequestForGrand: UserVerificationRequest

    private lateinit var userVerificationRequestForRevoke: UserVerificationRequest

    private lateinit var userVerificationResponse: UserVerificationResponse

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var network: Network

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var networkCapabilities: NetworkCapabilities

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userVerificationViewModel = UserVerificationViewModel(userVerificationRepository, credRepo, uuidRepository, preferenceRepository)
        userVerificationRequestForGrand = Gson().fromJson<UserVerificationRequest>("{\"requestId\":\"276fa8f3-810b-49d0-b0b5-d1f760271255\",\"pin\":\"1234\",\"scope\":\"consentrequest.approve\"}")
        userVerificationRequestForRevoke = Gson().fromJson<UserVerificationRequest>("{\"requestId\":\"276fa8f3-810b-49d0-b0b5-d1f760271255\",\"pin\":\"1234\",\"scope\":\"consent.revoke\"}")
        userVerificationResponse = Gson().fromJson<UserVerificationResponse>("{\"temporaryToken\":\"12345abc\"}")

        userVerificationViewModel.userVerificationResponse.observeForever(UserVerificationFetchObserver)

        // mock base URL
        `when`(context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)).thenReturn(
            sharedPreferences
        )
        `when`(sharedPreferences.getString(NETWORK_HOST, NetworkConstants.PROD_URL)).thenReturn(
            NetworkConstants.PROD_URL
        )
        // mock cache directory for interceptor
        `when`(context.cacheDir).thenReturn(File("text.txt"))
        // create your test retrofit client
        NetworkManager.createNetworkClient(context, credRepo, BuildConfig.DEBUG)

//        // mock net connection.
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            true
        )
    }

    @Test
    fun `should call verify user method of repository for grand consent`() {
        `when`(uuidRepository.generateUUID()).thenReturn("276fa8f3-810b-49d0-b0b5-d1f760271255")
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
        `when`(uuidRepository.generateUUID()).thenReturn("276fa8f3-810b-49d0-b0b5-d1f760271255")
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

    @Test
    fun `should Return Account Lock when wrong otp entered five times`(){
        `when`(uuidRepository.generateUUID()).thenReturn("276fa8f3-810b-49d0-b0b5-d1f760271255")
        `when`(userVerificationRepository.verifyUser(userVerificationRequestForGrand)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<UserVerificationResponse>
                callback.onResponse(call, Response.success(userVerificationResponse))
            }


        userVerificationViewModel.verifyUser("1234", ConsentScopeType.SCOPE_GRAND)
        Mockito.verify(userVerificationRepository).verifyUser(userVerificationRequestForGrand)
        Mockito.verify(call).enqueue(any())
        verify(UserVerificationFetchObserver, Mockito.times(1)).onChanged(Loading(true))
        verify(UserVerificationFetchObserver, Mockito.times(1)).onChanged(Loading(false))
        verify(UserVerificationFetchObserver, Mockito.times(3)).onChanged(PartialFailure(any()))
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(userVerificationRepository)
        Mockito.validateMockitoUsage()
    }
}