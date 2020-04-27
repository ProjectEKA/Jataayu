package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class VerifyIdentifierRequest(
    @SerializedName("sessionId") private val sessionId: String?,
    @SerializedName("value") private val value: String?
)
