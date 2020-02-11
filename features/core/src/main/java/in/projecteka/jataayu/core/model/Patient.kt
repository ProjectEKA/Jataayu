package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("referenceNumber") val referenceNumber: String,
    @SerializedName("display") val display: String,
    @SerializedName("careContexts") val careContexts: List<CareContext>,
    @SerializedName("matchedBy") val matchedBy: List<String>
)