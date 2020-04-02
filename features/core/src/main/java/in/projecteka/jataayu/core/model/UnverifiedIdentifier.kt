package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class UnverifiedIdentifier(

    @SerializedName("value") val value: String,
    @SerializedName("type") val type: String
)