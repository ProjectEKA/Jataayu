package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class RequestVerificationRequest(
    @SerializedName("identifierType") val identifierType: String,
    @SerializedName("identifier") val identifier: String
)
