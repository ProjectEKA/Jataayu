package `in`.projecteka.jataayu.user.account.remote

import `in`.projecteka.jataayu.core.model.CreateAccountRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.MyProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAccountApis {
    @GET("patients/links")
    fun getUserAccounts(): Call<LinkedAccountsResponse>

    @POST("patients/profile")
    fun createAccount(@Body createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse>

    @GET("patients/me")
    fun getMyProfile(): Call<MyProfile>
}
