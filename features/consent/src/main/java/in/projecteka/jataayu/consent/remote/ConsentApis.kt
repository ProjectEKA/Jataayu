package `in`.projecteka.jataayu.consent.remote

import `in`.projecteka.jataayu.consent.model.ConsentActionsRequest
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.RevokeConsentResponse
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import retrofit2.Call
import retrofit2.http.*

interface ConsentApis {
    @GET("consent-requests")
    fun getConsents(): Call<ConsentsListResponse>

    @GET("patients/links")
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>

    @POST("consent-requests/{request-id}/approve")
    fun approveConsent(@Path("request-id") requestId: String, @Body approveConsentRequest: ConsentArtifactRequest, @Header("Authorization") authToken: String): Call<ConsentArtifactResponse>

    @POST("revoke-consent")
    fun revokeConsent(@Body consentActionsRequest: ConsentActionsRequest): Call<RevokeConsentResponse>

    @GET("consent-requests/{request-id}/consent-artefacts")
    fun getGrantedConsentDetails(@Path("request-id") requestId: String): Call<List<GrantedConsentDetailsResponse>>

    @POST("consent-requests/{request-id}/deny")
    fun denyConsent(@Path("request-id") requestId: String): Call<Void>
}