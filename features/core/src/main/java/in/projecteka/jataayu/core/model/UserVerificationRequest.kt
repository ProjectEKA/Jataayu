package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class UserVerificationRequest(
    @SerializedName("pin") val pin : String,
    @SerializedName("scope") val scope : String
)