package `in`.org.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("referenceNumber") val referenceNumber: String,
    @SerializedName("authenticationType") val authenticationType: String,
    @SerializedName("meta") val meta: Meta
)