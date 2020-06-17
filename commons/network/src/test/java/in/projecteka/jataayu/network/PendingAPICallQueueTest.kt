package `in`.projecteka.jataayu.network

import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.PendingAPICallQueue
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.loading
import `in`.projecteka.jataayu.util.constant.NetworkConstants
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.sharedPref.NETWORK_HOST
import `in`.projecteka.jataayu.util.sharedPref.NETWORK_PREF
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class PendingAPICallQueueTest {

    private lateinit var pendingAPICallQueue: PendingAPICallQueue

    @Mock
    private lateinit var call: Call<ErrorResponse>

    @Mock
    private lateinit var liveData: PayloadLiveData<ErrorResponse>

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var network: Network

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var networkCapabilities: NetworkCapabilities

    @Mock
    private lateinit var classLoader: ClassLoader

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cacheDir: File


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cacheDir = File("test.txt")
        pendingAPICallQueue = PendingAPICallQueue()
        // mock base URL
        `when`(context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)).thenReturn(
            sharedPreferences
        )
        `when`(sharedPreferences.getString(NETWORK_HOST, NetworkConstants.PROD_URL)).thenReturn(
            NetworkConstants.PROD_URL
        )
        // mock cache directory for interceptor
        `when`(context.cacheDir).thenReturn(cacheDir)
        // create your test retrofit client
        NetworkManager.createNetworkClient(context, credentialsRepository, BuildConfig.DEBUG)

//        // mock net connection.
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
        `when`(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            true
        )

        doNothing().`when`(context).startActivity(ArgumentMatchers.any())
    }

    @After
    fun tearDown() {
        validateMockitoUsage()
    }

    @Test
    fun `test should add live data to pending queue`() {
        pendingAPICallQueue.add(liveData, call)
        assertTrue(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should clear pending queue`() {
        pendingAPICallQueue.clearQueue()
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should clear the queue after executing pending api call`() {
        pendingAPICallQueue.add(liveData, call)
        pendingAPICallQueue.execute<ErrorResponse>()
        verify(liveData, times(1)).loading(true)
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should execute all pending api call when internet connection available`() {
        pendingAPICallQueue.add(liveData, call)
        pendingAPICallQueue.execute<ErrorResponse>()
        verify(liveData, times(1)).fetch(call)
        verify(liveData, times(1)).loading(true)
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should start activity when there is no internet`() {
        `when`(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            false
        )
        `when`(context.classLoader).thenReturn(classLoader)
        liveData.fetch(call)
        verify(context, times(1)).startActivity(ArgumentMatchers.any())
        verify(call, times(0)).enqueue(ArgumentMatchers.any())
        assertTrue(pendingAPICallQueue.hasPendingAPICall)
    }
}



