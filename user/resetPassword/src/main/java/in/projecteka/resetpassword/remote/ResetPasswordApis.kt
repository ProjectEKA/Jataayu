package `in`.projecteka.resetpassword.remote

import `in`.projecteka.resetpassword.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ResetPasswordApis {
    @POST("patients/verifyotp")
    fun verifyIdentifier(@Body body: VerifyOTPRequest): Call<VerifyOTPResponse>

    @POST("patients/generateotp")
    fun generateOtp(@Body body: GenerateOTPRequest): Call<GenerateOTPResponse>

    @PUT("patients/profile/reset-password")
    fun resetPassword(@Body body: ResetPasswordRequest, @Header("Authorization") authToken: String?): Call<ResetPasswordResponse>
}
