package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.core.model.CreatePinRequest
import `in`.projecteka.jataayu.core.model.UpdatePinRequest
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call

interface UserVerificationRepository {
    fun createPin(createPinRequest: CreatePinRequest): Call<Void>
    fun updatePin(updatePinRequest: UpdatePinRequest, authToken: String?): Call<Void>
    fun verifyUser(userVerificationRequest: UserVerificationRequest): Call<UserVerificationResponse>
}

class UserVerificationRepositoryImpl(private val userVerificationApis: UserVerificationApis) : UserVerificationRepository {
    override fun createPin(createPinRequest: CreatePinRequest): Call<Void> {
        return userVerificationApis.getCreatePinResponse(createPinRequest)
    }

    override fun updatePin(updatePinRequest: UpdatePinRequest, authToken: String?): Call<Void> {
        return userVerificationApis.getUpdatePinResponse(updatePinRequest, authToken)
    }

    override fun verifyUser(userVerificationRequest: UserVerificationRequest): Call<UserVerificationResponse> {
        return userVerificationApis.getUserVerificationResponse(userVerificationRequest)
    }
}