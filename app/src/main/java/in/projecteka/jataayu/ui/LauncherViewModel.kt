package `in`.projecteka.jataayu.ui

import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.lifecycle.ViewModel

class LauncherViewModel(private val preferenceRepository: PreferenceRepository,
                        private val credentialsRepository: CredentialsRepository) : ViewModel() {

    val startLogin = SingleLiveEvent<Void>()
    val startDashboard = SingleLiveEvent<Void>()
    val startProvider = SingleLiveEvent<Void>()
    val startAccountCreation = SingleLiveEvent<Void>()
    val startIntroductionScreens = SingleLiveEvent<Void>()

    fun redirectIfNeeded() {
        when {
            preferenceRepository.shouldShowIntro -> {
                startIntroductionScreens.call()
            }
            preferenceRepository.hasProviders || preferenceRepository.isUserLoggedIn -> {
                startDashboard.call()
            }
            preferenceRepository.isUserAccountCreated -> {
                startDashboard.call()
            }
            preferenceRepository.isUserRegistered -> {
                startAccountCreation.call()
            }
            else -> {
                startLogin.call()
            }
        }
    }

    fun resetCredentials() {
        preferenceRepository.resetPreferences()
        credentialsRepository.reset()
    }
}