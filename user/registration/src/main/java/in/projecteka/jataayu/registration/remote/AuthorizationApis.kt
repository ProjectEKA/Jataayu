package `in`.projecteka.jataayu.registration.remote

import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApis {
    @POST("request-verification")
    fun requestVerification(@Body requestVerificationRequest: RequestVerificationRequest): Call<RequestVerificationResponse>

    @POST("verify-identifier")
    fun verifyIdentifier(@Body body: RequestVerificationResponse): Call<VerifyIdentifierResponse>
}
