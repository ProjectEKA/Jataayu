package `in`.projecteka.jataayu.consent.remote

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.RevokeConsentResponse
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ConsentApis {
    @GET("consent-requests")
    fun getConsents(): Call<ConsentsListResponse>

    @GET("patients/links")
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>

    @POST("/consent-requests/{request-id}/approve")
    fun approveConsent(@Path("request-id") requestId: String, @Body approveConsentRequest: ConsentArtifactRequest): Call<ConsentArtifactResponse>

    @POST("/revoke-consent")
    fun revokeConsent(@Body consent: Consent): Call<RevokeConsentResponse>
}