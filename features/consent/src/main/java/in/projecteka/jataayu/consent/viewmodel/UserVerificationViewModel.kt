package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.repository.CredentialsRepository

class UserVerificationViewModel(private val userVerificationRepository: UserVerificationRepository,
                                val credentialsRepository: CredentialsRepository) : BaseViewModel
    () {
    internal var createPinResponse = PayloadLiveData<Void>()
    internal var userVerificationResponse = PayloadLiveData<UserVerificationResponse>()

    fun createPin(pin: String) {
        createPinResponse.fetch(userVerificationRepository.createPin(pin))
    }

    fun verifyUser(pin: String) {
        userVerificationResponse.fetch(userVerificationRepository.verifyUser(pin))
    }
}
