package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class LoginOTPRequest(
    @SerializedName("username") val userName: String ,
    @SerializedName("sessionId") private val password: String,
    @SerializedName("otp") private val grantType: String
)