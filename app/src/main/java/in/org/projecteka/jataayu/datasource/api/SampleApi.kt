package `in`.org.projecteka.jataayu.datasource.api

import `in`.org.projecteka.jataayu.datasource.model.SampleModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SampleApi {
    @GET("todos/{id}")
    fun getResponse(@Path("id") id: Int): Call<SampleModel>
}