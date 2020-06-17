package `in`.projecteka.jataayu.network

import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.PendingAPICallQueue
import `in`.projecteka.jataayu.network.utils.fetch
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call

@RunWith(MockitoJUnitRunner::class)
class PendingAPICallQueueTest: KoinTest {

   private lateinit var pendingAPICallQueue: PendingAPICallQueue

    @Mock
    private lateinit var call: Call<ErrorResponse>


    @Mock
    private lateinit var liveData: PayloadLiveData<ErrorResponse>

    val networkManager : NetworkManager by inject()



    @Before
    fun setUp() {
        pendingAPICallQueue = PendingAPICallQueue()
        MockitoAnnotations.initMocks(this)
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
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }

    @Test
    fun `test should execute all pending api call`() {
        pendingAPICallQueue.add(liveData, call)
        pendingAPICallQueue.execute<ErrorResponse>()
        Mockito.verify(liveData, times(1)).fetch(call)
        assertFalse(pendingAPICallQueue.hasPendingAPICall)
    }
}



