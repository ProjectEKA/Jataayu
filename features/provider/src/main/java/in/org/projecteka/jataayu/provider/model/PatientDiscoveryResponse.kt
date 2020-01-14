package `in`.org.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class PatientDiscoveryResponse(
    @SerializedName("patient") val patient : Patient,
    @SerializedName("transactionId") val transactionId : String
)