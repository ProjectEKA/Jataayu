package `in`.org.projecteka.jataayu.consent.repository

import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.remote.ConsentApis
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import retrofit2.Call

interface ConsentRepository {
    fun getConsents(): Call<ConsentsListResponse>
    fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest): Call<ConsentArtifactResponse>
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
}

class ConsentRepositoryImpl(private val consentApi: ConsentApis) : ConsentRepository {
    override fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest): Call<ConsentArtifactResponse> {
        return consentApi.approveConsent(requestId, approveConsentRequest)
    }

    override fun getConsents(): Call<ConsentsListResponse> {
        return consentApi.getConsents()
    }
    override fun getLinkedAccounts(): Call<LinkedAccountsResponse> {
        return consentApi.getLinkedAccounts()
    }
}