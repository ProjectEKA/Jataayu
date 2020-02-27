package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.model.ConsentActionsRequest
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.RevokeConsentResponse
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import retrofit2.Call

interface ConsentRepository {
    fun getConsents(): Call<ConsentsListResponse>
    fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest): Call<ConsentArtifactResponse>
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
    fun revokeConsent(consentActionsRequest: ConsentActionsRequest): Call<RevokeConsentResponse>
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

    override fun revokeConsent(consentActionsRequest: ConsentActionsRequest): Call<RevokeConsentResponse> {
        return consentApi.revokeConsent(consentActionsRequest)
    }
}