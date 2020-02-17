package `in`.projecteka.jataayu.registration.repository

import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierRequest
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.projecteka.jataayu.registration.remote.AuthorizationApis
import retrofit2.Call

interface AuthorizationRepository {
 fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>
 fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse>
}

class AuthorizationRepositoryImpl(private val authApi: AuthorizationApis): AuthorizationRepository{
    override fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse> {
        return authApi.verifyIdentifier(body)
    }

    override fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>{
        return authApi.requestVerification(requestVerificationRequest)
    }

}
