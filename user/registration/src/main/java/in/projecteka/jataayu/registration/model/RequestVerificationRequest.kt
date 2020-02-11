package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class RequestVerificationRequest(
    @SerializedName("identifierType") private val identifierType: String,
    @SerializedName("identifier") private val identifier: String
)
