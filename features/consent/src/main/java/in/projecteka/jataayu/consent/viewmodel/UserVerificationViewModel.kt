package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.observeOn
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel

class UserVerificationViewModel(private val userVerificationRepository: UserVerificationRepository) : BaseViewModel() {
    internal var createPinResponse = PayloadLiveData<Void>()
    internal var userVerificationResponse = liveDataOf<UserVerificationResponse>()

    fun createPin(pin: String) {
        createPinResponse.fetch(userVerificationRepository.createPin(pin))
    }

    fun verifyUser(pin: String, responseCallback: ResponseCallback) {
        userVerificationRepository.verifyUser(pin).observeOn(userVerificationResponse, responseCallback)
    }
}
