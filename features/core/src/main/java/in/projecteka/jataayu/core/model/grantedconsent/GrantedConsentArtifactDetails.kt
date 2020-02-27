package `in`.projecteka.jataayu.core.model.grantedconsent

import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.Permission
import `in`.projecteka.jataayu.core.model.Purpose
import `in`.projecteka.jataayu.core.model.Requester
import com.google.gson.annotations.SerializedName

data class GrantedConsentArtifactDetails (

	@SerializedName("id") val consentId : String,
	@SerializedName("createdAt") val createdAt : String,
	@SerializedName("patient") val consentArtifactPatient : ConsentArtifactPatient,
//	@SerializedName("careContexts") val careContexts : List<CareReference>,
	@SerializedName("purpose") val purpose : Purpose,
	@SerializedName("hip") val hip : Hip,
	@SerializedName("hiu") val hiu : Hiu,
	@SerializedName("requester") val requester : Requester,
	@SerializedName("hiTypes") val hiTypes : List<String>,
	@SerializedName("permission") val permission : Permission
)