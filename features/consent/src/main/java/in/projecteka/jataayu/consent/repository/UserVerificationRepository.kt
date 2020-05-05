package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.core.model.CreatePinRequest
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call

interface UserVerificationRepository {
    fun createPin(createPinRequest: CreatePinRequest): Call<Void>
    fun verifyUser(userVerificationRequest: UserVerificationRequest): Call<UserVerificationResponse>
}

class UserVerificationRepositoryImpl(private val userVerificationApis: UserVerificationApis) : UserVerificationRepository {
    override fun createPin(createPinRequest: CreatePinRequest): Call<Void> {
        return userVerificationApis.getCreatePinResponse(createPinRequest)
    }

    override fun verifyUser(userVerificationRequest: UserVerificationRequest): Call<UserVerificationResponse> {
        return userVerificationApis.getUserVerificationResponse(userVerificationRequest)
    }
}