package `in`.projecteka.jataayu.core.model.approveconsent

import com.google.gson.annotations.SerializedName

data class CareReference(

    @SerializedName("patientReference") val patientReference: String? = null,
    @SerializedName("careContextReference") val careContextReference: String? = null
)