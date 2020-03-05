package `in`.projecteka.jataayu.registration.repository

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.registration.model.*
import `in`.projecteka.jataayu.registration.remote.AuthenticationApis
import retrofit2.Call

interface AuthenticationRepository {
    fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>
    fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse>
    fun login(body: LoginRequest): Call<CreateAccountResponse>
}

class AuthenticationRepositoryImpl(private val authApi: AuthenticationApis): AuthenticationRepository{
    override fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse> {
        return authApi.verifyIdentifier(body)
    }

    override fun login(body: LoginRequest): Call<CreateAccountResponse> {
        return authApi.login(body)
    }

    override fun requestVerification(requestVerificationRequest : RequestVerificationRequest): Call<RequestVerificationResponse>{
        return authApi.requestVerification(requestVerificationRequest)
    }

}
