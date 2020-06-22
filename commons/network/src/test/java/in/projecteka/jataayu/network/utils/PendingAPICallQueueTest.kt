package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.model.ErrorResponse
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class PendingAPICallQueueTest: KoinTest {

    private lateinit var pendingAPICallQueue: PendingAPICallQueue

    @Mock
    private lateinit var call: Call<ErrorResponse>

    @Mock
    private lateinit var liveData: PayloadLiveData<ErrorResponse>

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pendingAPICallQueue = PendingAPICallQueue()
        `when`(call.clone()).thenReturn(call)
        `when`(call.enqueue(any())).then {
            val callback = it.arguments[0] as Callback<ErrorResponse>
            callback.onResponse(call, Response.success(ErrorResponse(Error(0, ""))))
        }
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
        pendingAPICallQueue.add(liveData, call)
        pendingAPICallQueue.clearQueue()
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should execute all pending api call when internet connection available`() {
        `when`(liveData.canFetch()).thenReturn(true)
        pendingAPICallQueue.clearQueue()
        pendingAPICallQueue.add(liveData, call)
        pendingAPICallQueue.execute<ErrorResponse>()
        verify(call, times(1)).enqueue(ArgumentMatchers.any())
        verify(liveData).canFetch()
        verify(liveData, times(1)).loading(true)
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should start activity when there is no internet`() {
        `when`(liveData.canFetch()).thenReturn(false)
        liveData.fetch(call)
        verify(liveData).canFetch()
        verify(call, times(0)).enqueue(ArgumentMatchers.any())
        assertTrue(pendingAPICallQueue.hasPendingAPICall)
    }
}



