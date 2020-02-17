package `in`.projecteka.jataayu.registration.remote

import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierRequest
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApis {
    @POST("users/verify")
    fun requestVerification(@Body requestVerificationRequest: RequestVerificationRequest): Call<RequestVerificationResponse>

    @POST("users/permit")
    fun verifyIdentifier(@Body body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse>
}
