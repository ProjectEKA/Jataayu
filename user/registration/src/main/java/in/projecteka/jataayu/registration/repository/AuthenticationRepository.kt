package `in`.projecteka.jataayu.registration.repository

import `in`.projecteka.jataayu.registration.model.VerifyIdentifierRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.model.LoginRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.projecteka.jataayu.registration.remote.AuthenticationApis
import retrofit2.Call

interface AuthenticationRepository {
    fun requestVerification(requestVerificationRequest: RequestVerificationRequest): Call<RequestVerificationResponse>
    fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse>
    fun login(username: String, password: String, grantType: String): Call<CreateAccountResponse>
}

class AuthenticationRepositoryImpl(private val authApi: AuthenticationApis) : AuthenticationRepository {
    override fun verifyIdentifier(body: VerifyIdentifierRequest): Call<VerifyIdentifierResponse> {
        return authApi.verifyIdentifier(body)
    }

    override fun login(username: String, password: String, grantType: String): Call<CreateAccountResponse> {
        val loginRequest = LoginRequest(
            userName = username,
            password = password,
            grantType = grantType
        )
        return authApi.login(loginRequest)
    }

    override fun requestVerification(requestVerificationRequest: RequestVerificationRequest): Call<RequestVerificationResponse> {
        return authApi.requestVerification(requestVerificationRequest)
    }
}
