package `in`.projecteka.jataayu.ui

import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.lifecycle.ViewModel

class LauncherViewModel(private val preferenceRepository: PreferenceRepository) : ViewModel() {

    val startLogin = SingleLiveEvent<Void>()
    val startAccountFragments = SingleLiveEvent<Void>()
    val startProvider = SingleLiveEvent<Void>()
    val startAccountCreation = SingleLiveEvent<Void>()

    fun redirectIfNeeded() {

        when {
            preferenceRepository.hasProviders || preferenceRepository.isUserLoggedIn -> {
                startAccountFragments.call()
            }
            preferenceRepository.isUserAccountCreated -> {
                startProvider.call()
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
        preferenceRepository.resetCredentials()
    }

}