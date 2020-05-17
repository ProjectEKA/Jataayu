package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.registration.ui.activity.R
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ConsentManagerIDInputViewModelTest {

    private lateinit var consentManagerIDInputViewModel: ConsentManagerIDInputViewModel
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var registerEventObserver: Observer<Void>

    @Mock
    private lateinit var nextButtonEventObserver: Observer<Void>

    @Mock
    private lateinit var forgotPasswordObserver: Observer<Void>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        consentManagerIDInputViewModel = ConsentManagerIDInputViewModel()
        loginViewModel = LoginViewModel()
        consentManagerIDInputViewModel.onRegisterButtonClickEvent.observeForever(registerEventObserver)
        consentManagerIDInputViewModel.onNextButtonClickEvent.observeForever(nextButtonEventObserver)
        consentManagerIDInputViewModel.onForgetCMIDButtonClickEvent.observeForever(forgotPasswordObserver)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(registerEventObserver)
    }

    @Test
    fun `should trigger register event when register button clicked`() {
        consentManagerIDInputViewModel.onRegisterButtonClicked()
        verify(registerEventObserver, times(1)).onChanged(null)
    }

    @Test
    fun `should trigger next event event when next button clicked`() {
        consentManagerIDInputViewModel.onNextButtonClicked()
        verify(nextButtonEventObserver, times(1)).onChanged(null)
    }

    @Test
    fun `should trigger forgot cm id event event when forgot cm id button clicked`() {
        consentManagerIDInputViewModel.onForgotConsentManagerID()
        verify(forgotPasswordObserver, times(1)).onChanged(null)
    }

    @Test
    fun `should check mark default image source must be gray`() {
        assertEquals(R.drawable.ic_check_gray, consentManagerIDInputViewModel.cmIDCheckMarkImage.get())
    }

    @Test
    fun `should next enabled default value must be false`() {
        assertFalse(consentManagerIDInputViewModel.nextEnabled.get())
    }

    @Test
    fun `should disable next button when cm id is empty`() {
        consentManagerIDInputViewModel.inputUsernameLbl.set("")
        assertFalse(consentManagerIDInputViewModel.nextEnabled.get())
    }

    @Test
    fun `should enable next button when cm id is not empty`() {
        consentManagerIDInputViewModel.inputUsernameLbl.set("ab")
        consentManagerIDInputViewModel.onTextChanged("ab", 1,1, 2)
        assertTrue(consentManagerIDInputViewModel.nextEnabled.get())
    }

}