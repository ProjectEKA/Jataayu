package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Identifier(

    @SerializedName("value") val value: String,
    @SerializedName("type") val type: String,
    @SerializedName("system") val system: String
)