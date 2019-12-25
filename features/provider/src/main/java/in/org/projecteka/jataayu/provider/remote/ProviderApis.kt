package `in`.org.projecteka.jataayu.provider.remote

import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProviderApis {
    @GET("providers/")
    fun getProviders(@Query("name") name: String): Call<List<ProviderInfo>>

    @POST("patients/")
    fun getPatientAccounts(@Query("identifier") identifier: String): Call<PatientDiscoveryResponse>
}