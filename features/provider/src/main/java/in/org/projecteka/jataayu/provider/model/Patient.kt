package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.jataayu.core.model.CareContext
import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("referenceNumber") val referenceNumber: String,
    @SerializedName("display") val display: String,
    @SerializedName("careContexts") val careContexts: List<CareContext>
)