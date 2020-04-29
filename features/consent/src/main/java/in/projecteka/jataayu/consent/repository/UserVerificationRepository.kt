package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.core.model.UserVerificationResponse
import retrofit2.Call

interface UserVerificationRepository {
    fun createPin(pin: String): Call<Void>
    fun verifyUser(pin: String): Call<UserVerificationResponse>
}

class UserVerificationRepositoryImpl(private val userVerificationApis: UserVerificationApis) : UserVerificationRepository {
    override fun createPin(pin: String): Call<Void> {
        return userVerificationApis.getCreatePinResponse(mapOf("pin" to pin))   
    }

    override fun verifyUser(pin: String): Call<UserVerificationResponse> {
        return userVerificationApis.getUserVerificationResponse(mapOf("pin" to pin))
    }
}