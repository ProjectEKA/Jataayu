package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class UpdatePinRequest(
    @SerializedName("pin") val pin : String
)