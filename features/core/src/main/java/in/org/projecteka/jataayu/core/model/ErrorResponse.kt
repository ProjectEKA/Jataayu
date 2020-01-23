package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String,
    @SerializedName("requestId") val requestId: String,
    @SerializedName("status") val status: Int,
    @SerializedName("timestamp") val timestamp: String
)