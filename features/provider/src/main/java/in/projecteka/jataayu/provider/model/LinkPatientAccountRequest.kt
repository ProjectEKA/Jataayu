package `in`.projecteka.jataayu.provider.model

import `in`.projecteka.jataayu.core.model.Patient
import com.google.gson.annotations.SerializedName

data class LinkPatientAccountRequest(
    @SerializedName("requestId") val requestId : String,
    @SerializedName("patient") val patient : Patient,
    @SerializedName("transactionId") val transactionId : String
)