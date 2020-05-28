package `in`.projecteka.jataayu.provider.remote

import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.provider.model.*
import androidx.annotation.NonNull
import retrofit2.Call
import retrofit2.http.*

interface ProviderApis {
    @GET("providers")
    fun getProviders(@Query("name") name: String): Call<List<ProviderInfo>>

//    @POST("patients/discover/carecontexts")
    @POST("v1/care-contexts/discover")
    fun getPatientAccounts(@Body request: Request): Call<PatientDiscoveryResponse>

//    @POST("patients/link")
    @POST("v1/links/link/init")
    fun linkPatientAccounts(@Body linkPatientAccountRequest: LinkPatientAccountRequest): Call<LinkAccountsResponse>

//    @POST("patients/link/{referenceNumber}")
    @POST("v1/links/link/confirm/{referenceNumber}")
    fun verifyOtp(@Path("referenceNumber") referenceNumber: String, @Body otp: Otp): Call<SuccessfulLinkingResponse>

    @GET("providers/{provider-id}")
    fun getProvidersBy(@NonNull @Path("provider-id") providerId: String): Call<ProviderInfo>
}