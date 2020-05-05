package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class CreatePinRequest(
    @SerializedName("pin") val pin : String
)