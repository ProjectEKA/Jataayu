package `in`.projecteka.jataayu.consent.remote

import `in`.projecteka.jataayu.core.model.CreatePinRequest
import `in`.projecteka.jataayu.core.model.UpdatePinRequest
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserVerificationApis {
    @POST("patients/pin")
    fun getCreatePinResponse(@Body createPinRequest: CreatePinRequest): Call<Void>

    @POST("patients/change-pin")
    fun getUpdatePinResponse(@Body updatePinRequest: UpdatePinRequest,  @Header("Authorization") authToken: String?): Call<Void>

    @POST("patients/verify-pin")
    fun getUserVerificationResponse(@Body userVerificationRequest: UserVerificationRequest): Call<UserVerificationResponse>
}