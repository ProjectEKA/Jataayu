package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class LoginOTPSessionResponse(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("communicationMedium") val otpMedium: String,
    @SerializedName("communicationHint") val otpMediumValue: String,
    @SerializedName("communicationExpiry") val expiryInMinutes: String
)