package `in`.projecteka.jataayu.network.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: Error
)