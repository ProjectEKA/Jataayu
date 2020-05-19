package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.model.APIResponse
import `in`.projecteka.jataayu.registration.model.LoginOTPSessionResponse
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoginOtpViewModelTest {
    @Mock
    private lateinit var credentialsRepository: CredentialsRepository
    @Mock
    private lateinit var preferenceRepository: PreferenceRepository
    @Mock
    private lateinit var authenticationRepository: AuthenticationRepository
    @Mock
    private lateinit var loginByOTPResponseObserver: Observer<APIResponse<out CreateAccountResponse>?>

    @Mock
    private lateinit var otpSessionResponseObserver: Observer<APIResponse<out LoginOTPSessionResponse>?>

    @Mock
    private lateinit var errorDialogObserver: Observer<String>


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginOtpViewModel: LoginOtpViewModel
    private lateinit var otpSessionResponse: LoginOTPSessionResponse

    private val cmId =  "vk2704201@ncg"
    private val sessionId = "S12344"
    private val otp = "666666"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        loginOtpViewModel = LoginOtpViewModel(
            authenticationRepository,
            credentialsRepository,
            preferenceRepository
        )
    }

    @Test
    fun `should disable the login button by default`() {
        assertFalse(loginOtpViewModel.continueEnabled.get())
    }

    @Test
    fun `should enable the continue button if otp is not empty`() {
        loginOtpViewModel.otpText.set("123456")
        loginOtpViewModel.onTextChanged("123456", 1, 1, 6)
        assertTrue(loginOtpViewModel.continueEnabled.get())
    }

}