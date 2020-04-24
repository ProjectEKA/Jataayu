package `in`.projecteka.jataayu.consent.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConsentHostFragmentViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var redirectEventObserver: Observer<ConsentHostFragmentViewModel.Action>

    @Mock
    private lateinit var pullToRefreshObserver: Observer<Boolean>

    private lateinit var consentHostFragmentViewModel: ConsentHostFragmentViewModel

        @Before
        fun setUp() {

        MockitoAnnotations.initMocks(this)

        consentHostFragmentViewModel = ConsentHostFragmentViewModel()

            consentHostFragmentViewModel.redirectEvent.observeForever(redirectEventObserver)

            consentHostFragmentViewModel.pullToRefreshEvent.observeForever(pullToRefreshObserver)

    }

    @After
    fun tearDown(){
        consentHostFragmentViewModel.redirectEvent.removeObserver(redirectEventObserver)
        consentHostFragmentViewModel.pullToRefreshEvent.removeObserver(pullToRefreshObserver)
    }

    @Test
    fun `should set is refreshing and pull to refresh to false initially`(){
        consentHostFragmentViewModel.setUp()
        assertFalse(consentHostFragmentViewModel.isRefreshing.get())
        assertFalse(consentHostFragmentViewModel.pullToRefreshEvent.value!!)
    }

    @Test
    fun `should set is refreshing and pull to refresh to true on refresh`(){
        consentHostFragmentViewModel.onRefresh()
        assertTrue(consentHostFragmentViewModel.isRefreshing.get())
        assertTrue(consentHostFragmentViewModel.pullToRefreshEvent.value!!)
    }

    @Test
    fun `should select consents tab`(){
        consentHostFragmentViewModel.selectConsentsTab()
        verify(redirectEventObserver, times(1)).onChanged(ConsentHostFragmentViewModel.Action.SELECT_CONSENTS_TAB)
    }

    @Test
    fun `should show refreshing`(){
        consentHostFragmentViewModel.showRefreshing(true)
        assertTrue(consentHostFragmentViewModel.isRefreshing.get())
    }

    @Test
    fun `shouldn't show refreshing`(){
        consentHostFragmentViewModel.showRefreshing(false)
        assertFalse(consentHostFragmentViewModel.isRefreshing.get())
    }

    @Test
    fun `should refresh on pull`(){
        consentHostFragmentViewModel.onRefresh()
        verify(pullToRefreshObserver, times(1)).onChanged(true)
    }
}