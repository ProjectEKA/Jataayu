package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("requestId") val requestId: String,
    @SerializedName("hip") val hip: Hip,
    @SerializedName("unverifiedIdentifiers") val unverifiedIdentifiers: List<UnverifiedIdentifier>
)