package `in`.projecteka.jataayu.consent.remote

import `in`.projecteka.jataayu.core.model.CreatePinResponse
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserVerificationApis {
    @POST("/pin")
    fun getCreatePinResponse(@Body pin: String): Call<CreatePinResponse>

    @POST("/verify-user")
    fun getUserVerificationResponse(@Body pin: String): Call<UserVerificationResponse>
}