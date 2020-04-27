package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import retrofit2.Call

interface ConsentRepository {
    fun getConsents(): Call<ConsentsListResponse>
    fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest, authToken: String?): Call<ConsentArtifactResponse>
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
    fun revokeConsent(revokeConsentRequest: RevokeConsentRequest, authToken: String?): Call<Void>
    fun getGrantedConsentDetails(requestId: String): Call<List<GrantedConsentDetailsResponse>>
    fun denyConsent(requestId: String): Call<Void>
    fun getProviderBy(providerId: String): Call<ProviderInfo>

}

class ConsentRepositoryImpl(private val consentApi: ConsentApis) : ConsentRepository {
    override fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest, authToken: String?):
            Call<ConsentArtifactResponse> {
        return consentApi.approveConsent(requestId, approveConsentRequest, authToken)
    }

    override fun getConsents(): Call<ConsentsListResponse> {
        return consentApi.getConsents()
    }
    override fun getLinkedAccounts(): Call<LinkedAccountsResponse> {
        return consentApi.getLinkedAccounts()
    }

    override fun revokeConsent(revokeConsentRequest: RevokeConsentRequest, authToken: String?): Call<Void> {
        return consentApi.revokeConsent(revokeConsentRequest, authToken)
    }

    override fun getGrantedConsentDetails(requestId: String): Call<List<GrantedConsentDetailsResponse>> {
        return consentApi.getGrantedConsentDetails(requestId)
    }

    override fun denyConsent(requestId: String): Call<Void> {
        return consentApi.denyConsent(requestId)
    }

    override fun getProviderBy(providerId: String): Call<ProviderInfo> {
        return consentApi.getProvidersBy(providerId)
    }
}