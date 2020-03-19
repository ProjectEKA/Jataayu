package `in`.projecteka.jataayu.consent.remote

import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserVerificationApis {
    @POST("patients/pin")
    fun getCreatePinResponse(@Body pin: String): Call<Void>

    @POST("patients/verify-pin")
    fun getUserVerificationResponse(@Body pin: String): Call<UserVerificationResponse>
}