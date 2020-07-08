package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("first") val first: String,
    @SerializedName("last") val last: String?,
    @SerializedName("middle") val middle: String?
)