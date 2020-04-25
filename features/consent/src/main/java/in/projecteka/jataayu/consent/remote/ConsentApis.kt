package `in`.projecteka.jataayu.consent.remote

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import androidx.annotation.NonNull
import retrofit2.Call
import retrofit2.http.*

interface ConsentApis {
    @GET("consent-requests")
    fun getConsents(): Call<ConsentsListResponse>

    @GET("patients/links")
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>

    @POST("consent-requests/{request-id}/approve")
    fun approveConsent(@Path("request-id") requestId: String, @Body approveConsentRequest: ConsentArtifactRequest, @Header("Authorization") authToken: String): Call<ConsentArtifactResponse>

    @POST("consents/revoke")
    fun revokeConsent(@Body revokeConsentRequest: RevokeConsentRequest, @Header("Authorization") authToken: String): Call<Void>

    @GET("consent-requests/{request-id}/consent-artefacts")
    fun getGrantedConsentDetails(@Path("request-id") requestId: String): Call<List<GrantedConsentDetailsResponse>>

    @POST("consent-requests/{request-id}/deny")
    fun denyConsent(@Path("request-id") requestId: String): Call<Void>

    @Headers("Cache-Control: max-age=300")
    @GET("providers/{provider-id}")
    fun getProvidersBy(@NonNull @Path("provider-id") providerId: String): Call<ProviderInfo>
}