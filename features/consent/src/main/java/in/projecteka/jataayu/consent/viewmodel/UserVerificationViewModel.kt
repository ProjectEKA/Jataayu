package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.model.CreatePinResponse
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.observeOn
import `in`.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel

class UserVerificationViewModel(private val userVerificationRepository: UserVerificationRepository) : ViewModel() {
    internal var createPinResponse = liveDataOf<CreatePinResponse>()
    internal var userVerificationResponse = liveDataOf<UserVerificationResponse>()

    fun createPin(pin: String, responseCallback: ResponseCallback) {
        userVerificationRepository.createPin(pin).observeOn(createPinResponse, responseCallback)
    }

    fun verifyUser(pin: String, responseCallback: ResponseCallback) {
        userVerificationRepository.verifyUser(pin).observeOn(userVerificationResponse, responseCallback)
    }
}
