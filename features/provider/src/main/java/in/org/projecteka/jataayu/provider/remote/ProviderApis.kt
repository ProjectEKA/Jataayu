package `in`.org.projecteka.jataayu.provider.remote

import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProviderSearchApi {
    @GET("providers/")
    fun getProvider(@Query("name") name: String): Call<List<ProviderInfo>>
}