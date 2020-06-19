package `in`.projecteka.jataayu.network.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import junit.framework.TestCase.assertFalse
import org.greenrobot.eventbus.EventBus
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetworkBoundLiveDataTest {

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    private lateinit var registry: LifecycleRegistry

    private var networkBoundLiveData = NetworkBoundLiveData<Boolean>()

    @Mock
    private lateinit var observer: Observer<Boolean>

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        registry = LifecycleRegistry(lifecycleOwner)
        registry.handleLifecycleEvent(ON_RESUME)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(registry)
    }

    @After
    fun tearDown() {
        EventBus.getDefault().unregister(networkBoundLiveData)
    }

    @Test
    fun `test should register network status update in event bus on observing live data`() {
        networkBoundLiveData.observe(lifecycleOwner,observer)
        assertTrue(EventBus.getDefault().isRegistered(networkBoundLiveData))
    }

    @Test
    fun `test should register network status update in event bus on observing live data forever`() {
        networkBoundLiveData.observe(lifecycleOwner,observer)
        assertTrue(EventBus.getDefault().isRegistered(networkBoundLiveData))
    }

    @Test
    fun `test should unregister network status update in event bus on removing live data observer`() {
        networkBoundLiveData.removeObserver(observer)
        assertFalse(EventBus.getDefault().isRegistered(networkBoundLiveData))
    }

    @Test
    fun `test should unregister network status update in event bus if life cycle event changes to destroyed`() {
        registry.handleLifecycleEvent(ON_DESTROY)
        assertFalse(EventBus.getDefault().isRegistered(networkBoundLiveData))
    }

    @Test
    fun `test should return can fetch default to true`() {
        assertTrue(networkBoundLiveData.canFetch())
    }

    @Test
    fun `test should return can fetch to true if network status is online`() {
        networkBoundLiveData.updateNetworkStatus(NetworkStatus(true))
        assertTrue(networkBoundLiveData.canFetch())
    }

    @Test
    fun `test should return can fetch to false if network status is offline`() {
        networkBoundLiveData.updateNetworkStatus(NetworkStatus(false))
        assertFalse(networkBoundLiveData.canFetch())
    }
}