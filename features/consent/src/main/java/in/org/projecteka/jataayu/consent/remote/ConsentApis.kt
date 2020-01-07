package `in`.org.projecteka.jataayu.consent.remote

import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import retrofit2.Call
import retrofit2.http.GET

interface ConsentApis {
    @GET("requests/")
    fun getConsents(): Call<ConsentsListResponse>
}