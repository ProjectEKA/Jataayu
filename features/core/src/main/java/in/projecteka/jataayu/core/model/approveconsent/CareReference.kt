package `in`.projecteka.jataayu.core.model.approveconsent

import com.google.gson.annotations.SerializedName

data class CareReference (

	@SerializedName("patientReference") val patientReference : String,
	@SerializedName("careContextReference") val careContextReference : String
)