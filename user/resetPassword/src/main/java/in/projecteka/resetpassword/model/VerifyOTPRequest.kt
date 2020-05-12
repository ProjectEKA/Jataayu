package `in`.projecteka.resetpassword.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("sessionId") private val sessionId: String?,
    @SerializedName("value") private val value: String?
)
