package `in`.projecteka.jataayu.network

import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.PendingAPICallQueue
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.loading
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit

@RunWith(MockitoJUnitRunner::class)
class PendingAPICallQueueTest: KoinTest {

    private lateinit var pendingAPICallQueue: PendingAPICallQueue

    @Mock
    private lateinit var call: Call<ErrorResponse>

    @Mock
    private lateinit var liveData: PayloadLiveData<ErrorResponse>

    @Mock
    private lateinit var context: Context

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkManager: NetworkManager



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pendingAPICallQueue = PendingAPICallQueue()
        startKoin {
            loadKoinModules(networkModule)
        }
        `when`(call.clone()).thenReturn(call)
//          doNothing().`when`(context).startActivity(ArgumentMatchers.any())
    }

    @After
    fun tearDown() {
        validateMockitoUsage()
        stopKoin()
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
        //`when`(networkManager.hasInternetConnection()).thenReturn(true)
        pendingAPICallQueue.add(liveData, call)
        pendingAPICallQueue.execute<ErrorResponse>()
        verify(liveData, times(1)).fetch(call)
        verify(liveData, times(2)).loading(true)
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    fun `test should start activity when there is no internet`() {
        `when`(networkManager.hasInternetConnection()).thenReturn(false)
        liveData.fetch(call)
        verify(call).enqueue(ArgumentMatchers.any())
        verify(context, times(1)).startActivity(ArgumentMatchers.any())
        verify(call, times(0)).enqueue(ArgumentMatchers.any())
        assertTrue(pendingAPICallQueue.hasPendingAPICall)
    }
}

// for testing
val networkModule = module {
    single { NetworkManager(get()) }
    single { get<NetworkManager>().createNetworkClient(get(),BuildConfig.DEBUG) }
    single<Converter<ResponseBody, ErrorResponse>> {
        get<Retrofit>().responseBodyConverter(
            ErrorResponse::class.java,
            arrayOfNulls<Annotation>(0)
        )
    }
}


