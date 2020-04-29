package `in`.projecteka.jataayu.dashboard

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.ui.dashboard.DashboardViewModel
import android.view.MenuItem
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DashboardViewModelTest {

    @Mock
    private lateinit var showFragmentObserver: Observer<DashboardViewModel.Show>

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    lateinit var lifecycleRegistry: LifecycleRegistry

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewModel: DashboardViewModel = DashboardViewModel()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycleRegistry)
    }

    @Test
    fun `no event is observed without redirect being called when lifecycle owner is created`() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        viewModel.showFragmentEvent.observe(lifecycleOwner, showFragmentObserver)

        verify(showFragmentObserver, never()).onChanged(any())

    }

    @Test
    fun `no event is observed when lifecycle owner is destroyed`() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModel.showFragmentEvent.observe(lifecycleOwner, showFragmentObserver)

        verify(showFragmentObserver, never()).onChanged(any())

    }

    @Test
    fun `redirected to home when navigation is clicked to show UserAccounts`(){
        val menuItem = mock(MenuItem::class.java)
        `when`(menuItem.itemId).thenReturn(R.id.action_accounts)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        viewModel.showFragmentEvent.observe(lifecycleOwner,showFragmentObserver)

        viewModel.onNavigationItemSelected(menuItem)

        verify(showFragmentObserver, times(1)).onChanged(DashboardViewModel.Show.USER_ACCOUNT)
        verify(showFragmentObserver, never()).onChanged(DashboardViewModel.Show.CONSENT_HOME)
    }

    @Test
    fun `redirected to consents when navigation is clicked to show Consents`(){
        val menuItem = mock(MenuItem::class.java)
        `when`(menuItem.itemId).thenReturn(R.id.action_consents)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        viewModel.showFragmentEvent.observe(lifecycleOwner,showFragmentObserver)

        viewModel.onNavigationItemSelected(menuItem)

        verify(showFragmentObserver, times(1)).onChanged(DashboardViewModel.Show.CONSENT_HOME)
        verify(showFragmentObserver, never()).onChanged(DashboardViewModel.Show.USER_ACCOUNT)
    }

    @Test
    fun `no redirection when neither consent or home is clicked`(){
        val menuItem = mock(MenuItem::class.java)
        `when`(menuItem.itemId).thenReturn(R.id.action_settings)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        viewModel.showFragmentEvent.observe(lifecycleOwner,showFragmentObserver)

        viewModel.onNavigationItemSelected(menuItem)

        verify(showFragmentObserver, never()).onChanged(DashboardViewModel.Show.CONSENT_HOME)
        verify(showFragmentObserver, never()).onChanged(DashboardViewModel.Show.USER_ACCOUNT)
    }
}