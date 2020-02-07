package `in`.org.projecteka.jataayu.registration.viewmodel

import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.network.utils.observeOn
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.org.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.org.projecteka.jataayu.registration.repository.AuthorizationRepository
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
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

    fun verifyIdentifier(requestVerificationResponse: RequestVerificationResponse, responseCallback: ResponseCallback) {
        repository.verifyIdentifier(requestVerificationResponse).observeOn(verifyIdentifierResponse, responseCallback)
    }

    fun getMobileNumber(mobileNumber: String): String {
        return getCountryCode() + mobileNumber
    }

    fun getCountryCode(): String? {
        return INDIA_COUNTRY_CODE + COUNTRY_CODE_SEPARATOR
    }
}