package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("value") val value: String?
)
