package `in`.org.projecteka.jataayu.consent.remote

import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ConsentApis {
    @GET("consent-requests")
    fun getConsents(): Call<ConsentsListResponse>

    @GET("patients/links")
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
}