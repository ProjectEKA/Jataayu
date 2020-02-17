package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.observeOn
import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierRequest
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.projecteka.jataayu.registration.repository.AuthorizationRepository
import `in`.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel

class RegistrationViewModel(val repository: AuthorizationRepository) : ViewModel() {

    companion object{
        const val INDIA_COUNTRY_CODE = "+91"
        const val COUNTRY_CODE_SEPARATOR = " - "
        const val MOBILE_IDENTIFIER_TYPE = "mobile"
    }

    var requestVerificationResponse = liveDataOf<RequestVerificationResponse>()

    private var verifyIdentifierResponse = liveDataOf<VerifyIdentifierResponse>()

    fun requestVerification(identifierType: String, identifier: String, responseCallback: ResponseCallback) {
        repository.requestVerification(RequestVerificationRequest(identifierType, identifier)).observeOn(requestVerificationResponse, responseCallback)
    }

    fun verifyIdentifier(sessionId: String, otp: String, responseCallback: ResponseCallback) {
        repository.verifyIdentifier(VerifyIdentifierRequest(sessionId, otp)).observeOn(verifyIdentifierResponse, responseCallback)
    }

    fun getMobileNumber(mobileNumber: String): String {
        return getCountryCode() + mobileNumber
    }

    fun getCountryCode(): String? {
        return INDIA_COUNTRY_CODE + COUNTRY_CODE_SEPARATOR
    }
}