package `in`.projecteka.jataayu.user.account.remote

import `in`.projecteka.jataayu.core.model.*
import androidx.annotation.NonNull
import retrofit2.Call
import retrofit2.http.*

interface UserAccountApis {
    @GET("patients/links")
    fun getUserAccounts(): Call<LinkedAccountsResponse>

    @POST("patients/profile")
    fun createAccount(@Body createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse>

    @GET("patients/me")
    fun getMyProfile(): Call<MyProfile>

    @Headers("Cache-Control: max-age=86400")
    @GET("providers/{provider-id}")
    fun getProvidersByID(@NonNull @Path("provider-id") providerId: String): Call<ProviderInfo>
}
