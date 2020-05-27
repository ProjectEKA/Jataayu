package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.registration.model.VerifyIdentifierRequest
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository

class RegistrationActivityViewModel(
    val authentiationRepo: AuthenticationRepository,
    val preferenceRepository: PreferenceRepository,
    val credentialsRepository: CredentialsRepository
) : BaseViewModel() {

    internal enum class Show {
        REGISTRATION,
        VERIFICATION,
        NEXT
    }

    val requestVerificationResponseLiveData = PayloadLiveData<RequestVerificationResponse>()

    val verifyIdentifierResponseLiveData = PayloadLiveData<VerifyIdentifierResponse>()

    private var requestIdentifierRequest: RequestVerificationRequest? = null
    private var sessionId: String? = null

    internal val redirectTo = SingleLiveEvent<Show>()

    fun setup() {
        redirectTo.value = Show.REGISTRATION
    }

    fun getIdentifierValue() = requestIdentifierRequest?.identifier

    fun requestVerification(request: RequestVerificationRequest) {
        requestIdentifierRequest = request
        requestVerificationResponseLiveData.fetch(authentiationRepo.requestVerification(request))
    }

    fun verifyRequest(otp: String) {
        verifyIdentifierResponseLiveData.fetch(
            authentiationRepo.verifyIdentifier(
                VerifyIdentifierRequest(sessionId = sessionId, value = otp)
            )
        )
    }

    fun redirectToRegistrationScreen() {
        redirectTo.value = Show.REGISTRATION
    }

    fun redirectToOtpScreen(sessionId: String?) {
        this.sessionId = sessionId
        redirectTo.value = Show.VERIFICATION
    }

    fun redirectToNext() {
        redirectTo.value = Show.NEXT
    }
}