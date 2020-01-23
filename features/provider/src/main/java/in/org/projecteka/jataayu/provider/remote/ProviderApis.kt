package `in`.org.projecteka.jataayu.provider.remote

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.core.model.Request
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.org.projecteka.jataayu.provider.model.Token
import retrofit2.Call
import retrofit2.http.*

interface ProviderApis {
    @GET("providers")
    fun getProviders(@Query("name") name: String): Call<List<ProviderInfo>>

    @POST("patients/discover")
    fun getPatientAccounts(@Body request: Request): Call<PatientDiscoveryResponse>

    @POST("patients/link")
    fun linkPatientAccounts(@Body patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse>

    @POST("patients/link/{referenceNumber}")
    fun verifyOtp(@Path("referenceNumber") referenceNumber: String, @Body token: Token): Call<SuccessfulLinkingResponse>
}