package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class GenerateOTPResponse(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("otpMedium") val otpMedium: String,
    @SerializedName("otpMediumValue") val otpMediumValue: String,
    @SerializedName("expiryInMinutes") val expiryInMinutes: Int
)