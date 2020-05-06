package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.core.model.CreatePinRequest
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.UuidRepository

class UserVerificationViewModel(private val userVerificationRepository: UserVerificationRepository,
                                val credentialsRepository: CredentialsRepository,
                                val uuidRepository: UuidRepository) : BaseViewModel
    () {

    companion object {
        const val VALUE_SCOPE_GRAND = "consentrequest.approve"
        const val VALUE_SCOPE_REVOKE = "consent.revoke"
    }

    internal var createPinResponse = PayloadLiveData<Void>()
    internal var userVerificationResponse = PayloadLiveData<UserVerificationResponse>()

    fun createPin(pin: String) {
        createPinResponse.fetch(userVerificationRepository.createPin(CreatePinRequest(pin)))
    }

    fun verifyUser(pin: String, scope: ConsentScopeType) {
        var scopeType: String? = null
        if (scope == ConsentScopeType.SCOPE_GRAND){
            scopeType = VALUE_SCOPE_GRAND
        } else {
            scopeType = VALUE_SCOPE_REVOKE
        }
        userVerificationResponse.fetch(userVerificationRepository.verifyUser(UserVerificationRequest(uuidRepository.generateUUID(), pin, scopeType)))
    }
}
