package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("referenceNumber") val referenceNumber: String,
    @SerializedName("careContexts") val careContexts: List<CareContext>
)