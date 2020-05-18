package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.UserVerificationRepository
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.core.model.CreatePinRequest
import `in`.projecteka.jataayu.core.model.UpdatePinRequest
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.UUIDRepository
import androidx.databinding.ObservableField

class UserVerificationViewModel(private val userVerificationRepository: UserVerificationRepository,
                                val credentialsRepository: CredentialsRepository,
                                val uuidRepository: UUIDRepository, val preferenceRepository: PreferenceRepository) : BaseViewModel
    () {

    companion object {
        const val VALUE_SCOPE_GRAND = "consentrequest.approve"
        const val VALUE_SCOPE_REVOKE = "consent.revoke"
        const val VALUE_SCOPE_PIN_VERIFY = "profile.changepin"
        const val KEY_SCOPE_TYPE = "scope_type"
    }

    var scopeType = ObservableField<ConsentScopeType>(ConsentScopeType.SCOPE_GRAND)

    internal var createPinResponse = PayloadLiveData<Void>()
    internal var updatePinResponse = PayloadLiveData<Void>()
    internal var userVerificationResponse = PayloadLiveData<UserVerificationResponse>()

    fun createPin(pin: String) {
        createPinResponse.fetch(userVerificationRepository.createPin(CreatePinRequest(pin)))
    }

    fun updatePin(pin: String) {
        updatePinResponse.fetch(userVerificationRepository.updatePin(UpdatePinRequest(pin), credentialsRepository.consentTemporaryToken))
    }

    fun verifyUser(pin: String, scope: ConsentScopeType) {
        var scopeType: String? = null
        if (scope == ConsentScopeType.SCOPE_GRAND){
            scopeType = VALUE_SCOPE_GRAND
        } else if (scope == ConsentScopeType.SCOPE_PIN_VERIFY) {
            scopeType = VALUE_SCOPE_PIN_VERIFY
        }else {
            scopeType = VALUE_SCOPE_REVOKE
        }
        userVerificationResponse.fetch(userVerificationRepository.verifyUser(UserVerificationRequest(uuidRepository.generateUUID(), pin, scopeType)))
    }
}
