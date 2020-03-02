package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.core.model.CreatePinResponse
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call

interface UserVerificationRepository {
    fun createPin(pin: String): Call<CreatePinResponse>
    fun verifyUser(pin: String): Call<UserVerificationResponse>
}

class UserVerificationRepositoryImpl(private val userVerificationApis: UserVerificationApis) : UserVerificationRepository {
    override fun createPin(pin: String): Call<CreatePinResponse> {
        return userVerificationApis.getCreatePinResponse(pin)
    }

    override fun verifyUser(pin: String): Call<UserVerificationResponse> {
        return userVerificationApis.getUserVerificationResponse(pin)
    }
}