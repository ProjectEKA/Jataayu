package `in`.projecteka.jataayu.registration.repository

import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierRequest
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.projecteka.jataayu.registration.remote.AuthenticationApis
import retrofit2.Call

interface AuthenticationRepository {
 fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>
 fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse>
}

class AuthenticationRepositoryImpl(private val authApi: AuthenticationApis): AuthenticationRepository{
    override fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse> {
        return authApi.verifyIdentifier(body)
    }

    override fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>{
        return authApi.requestVerification(requestVerificationRequest)
    }

}
