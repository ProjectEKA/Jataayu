package `in`.org.projecteka.jataayu.consent.remote

import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.model.LinkedAccountsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ConsentApis {
    @GET("consent-requests")
    fun getConsents(): Call<ConsentsListResponse>

    @POST("consent/accounts")
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
}