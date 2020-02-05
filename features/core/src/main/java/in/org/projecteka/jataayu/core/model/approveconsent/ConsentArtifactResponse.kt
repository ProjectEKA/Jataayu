package `in`.org.projecteka.jataayu.core.model.approveconsent

import com.google.gson.annotations.SerializedName

data class ConsentArtifactResponse (

	@SerializedName("consents") val consents : List<ApprovedConsent>
)