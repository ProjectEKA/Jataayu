package `in`.projecteka.jataayu.registration.remote

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.registration.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApis {
    @POST("users/verify")
    fun requestVerification(@Body requestVerificationRequest: RequestVerificationRequest): Call<RequestVerificationResponse>

    @POST("users/permit")
    fun verifyIdentifier(@Body body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse>

    @POST("sessions")
    fun login(@Body loginRequest: LoginRequest): Call<CreateAccountResponse>
}
