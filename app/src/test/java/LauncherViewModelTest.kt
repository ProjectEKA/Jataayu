
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
    fun `should start dashboard screen if user already loggedIn but no providers`() {

        `when`(preferenceRepository.isUserLoggedIn).thenReturn(true)
        `when`(preferenceRepository.hasProviders).thenReturn(false)

        launcherViewModel.startDashboard.observeForever {
            assertTrue( "redirected to dashboard since user is already logged in", true)
        }
        launcherViewModel.redirectIfNeeded()
        verify(preferenceRepository).hasProviders
        verify(preferenceRepository).isUserLoggedIn
    }

    @Test
    fun `should start dashboard screen if user has providers`() {

        `when`(preferenceRepository.hasProviders).thenReturn(true)
        launcherViewModel.startDashboard.observeForever {
            assertTrue(true)
        }
        launcherViewModel.redirectIfNeeded()
        verify(preferenceRepository).hasProviders
    }

    @Test
    fun `should start provider screen if user has just created account`() {

        `when`(preferenceRepository.hasProviders).thenReturn(false)
        `when`(preferenceRepository.isUserLoggedIn).thenReturn(false)
        `when`(preferenceRepository.isUserAccountCreated).thenReturn(true)

        launcherViewModel.startProvider.observeForever {
            assertTrue(true)
        }
        launcherViewModel.redirectIfNeeded()
        verify(preferenceRepository).hasProviders
        verify(preferenceRepository).isUserLoggedIn
        verify(preferenceRepository).isUserAccountCreated
    }

    @Test
    fun `should start account creation screen if user has just registered account`() {

        `when`(preferenceRepository.hasProviders).thenReturn(false)
        `when`(preferenceRepository.isUserLoggedIn).thenReturn(false)
        `when`(preferenceRepository.isUserAccountCreated).thenReturn(false)
        `when`(preferenceRepository.isUserRegistered).thenReturn(true)

        launcherViewModel.startAccountCreation.observeForever {
            assertTrue(true)
        }
        launcherViewModel.redirectIfNeeded()
        verify(preferenceRepository).hasProviders
        verify(preferenceRepository).isUserLoggedIn
        verify(preferenceRepository).isUserAccountCreated
        verify(preferenceRepository).isUserRegistered
    }

    @Test
    fun `should start login page if user don't have shared preferences`() {

        launcherViewModel.startLogin.observeForever {
            assertTrue(true)
        }
        launcherViewModel.redirectIfNeeded()

        verify(preferenceRepository).hasProviders
        verify(preferenceRepository).isUserLoggedIn
        verify(preferenceRepository).isUserAccountCreated
        verify(preferenceRepository).isUserRegistered
    }
}