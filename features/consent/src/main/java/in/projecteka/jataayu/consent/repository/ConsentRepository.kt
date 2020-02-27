package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import retrofit2.Call

interface ConsentRepository {
    fun getConsents(): Call<ConsentsListResponse>
    fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest): Call<ConsentArtifactResponse>
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
    fun getGrantedConsentDetails(requestId: String): Call<List<GrantedConsentDetailsResponse>>
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

    override fun getGrantedConsentDetails(requestId: String): Call<List<GrantedConsentDetailsResponse>> {
        return consentApi.getGrantedConsentDetails(requestId)
    }
}