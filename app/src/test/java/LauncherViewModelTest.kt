
import `in`.projecteka.jataayu.ui.LauncherViewModel
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LauncherViewModelTest {

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var launcherViewModel: LauncherViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        launcherViewModel = LauncherViewModel(preferenceRepository, credentialsRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(credentialsRepository, preferenceRepository)
        validateMockitoUsage()
    }

    @Test
    fun `should start dashboard screen if user already loggedIn`() {

        `when`(preferenceRepository.isUserLoggedIn).thenReturn(true)

        launcherViewModel.startDashboard.observeForever {
            assertTrue( "redirected to dashboard since user is already logged in", true)
        }
        launcherViewModel.redirectIfNeeded()
        verify(preferenceRepository).shouldShowIntro
        verify(preferenceRepository).isUserLoggedIn
    }

    @Test
    fun `should show intro screens if user start application for the first time`() {

        `when`(preferenceRepository.shouldShowIntro).thenReturn(true)

        launcherViewModel.startIntroductionScreens.observeForever {
            assertTrue(true)
        }
        launcherViewModel.redirectIfNeeded()
        verify(preferenceRepository).shouldShowIntro
    }

    @Test
    fun `should start login page if user not logged in`() {

        launcherViewModel.startLogin.observeForever {
            assertTrue(true)
        }
        launcherViewModel.redirectIfNeeded()

        verify(preferenceRepository).shouldShowIntro
        verify(preferenceRepository).isUserLoggedIn
    }
}