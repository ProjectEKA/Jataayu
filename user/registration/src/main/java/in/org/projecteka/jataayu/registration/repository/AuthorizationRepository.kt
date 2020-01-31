package `in`.org.projecteka.jataayu.registration.repository

import `in`.org.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.org.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.org.projecteka.jataayu.registration.remote.AuthorizationApis
import retrofit2.Call

interface AuthorizationRepository {
 fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>
 fun verifyIdentifier(body: RequestVerificationResponse): Call<VerifyIdentifierResponse>
}

class AuthorizationRepositoryImpl(private val authApi: AuthorizationApis): AuthorizationRepository{
    override fun verifyIdentifier(body: RequestVerificationResponse): Call<VerifyIdentifierResponse> {
        return authApi.verifyIdentifier(body)
    }

    override fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>{
        return authApi.requestVerification(requestVerificationRequest)
    }

}
