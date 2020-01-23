package `in`.org.projecteka.jataayu.provider.remote

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.core.model.Request
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProviderApis {
    @GET("providers")
    fun getProviders(@Query("name") name: String): Call<List<ProviderInfo>>

    @POST("patients/discover")
    fun getPatientAccounts(@Body request: Request): Call<PatientDiscoveryResponse>

    @POST("patients/link")
    fun linkPatientAccounts(@Body patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse>
}