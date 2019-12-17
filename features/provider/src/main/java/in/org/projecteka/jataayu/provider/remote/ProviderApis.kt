package `in`.org.projecteka.jataayu.provider.remote

import `in`.org.projecteka.jataayu.provider.model.Patient
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProviderApis {
    @GET("providers/")
    fun getProviders(@Query("name") name: String): Call<List<ProviderInfo>>

    @GET("patients/")
    fun getPatients(@Query("identifier") identifier: String): Call<List<Patient>>
}