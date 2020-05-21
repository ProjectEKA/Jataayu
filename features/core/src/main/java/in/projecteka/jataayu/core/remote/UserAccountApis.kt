package `in`.projecteka.jataayu.core.remote

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

    @POST("logout")
    fun logout(@Body body: Map<String, String>): Call<Void>

    @Headers("Cache-Control: max-age=86400")
    @GET("providers/{provider-id}")
    fun getProvidersBy(@NonNull @Path("provider-id") providerId: String): Call<ProviderInfo>

    @GET("patients/profile/loginmode")
    fun getLoginMode(@Query("userName") userName: String): Call<LoginType>

    @POST("recoverCmid")
    fun recoverCmid(@Body recoverCmidRequest: RecoverCmidRequest): Call<RecoverCmidResponse>
}
