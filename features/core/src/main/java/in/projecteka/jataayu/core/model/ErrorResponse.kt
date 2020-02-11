package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: Error
)