package `in`.projecteka.jataayu.core.model.approveconsent

import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.Permission
import com.google.gson.annotations.SerializedName

data class ConsentArtifact (

	@SerializedName("hiTypes") val hiTypes : List<String>,
	@SerializedName("hip") val hip : Hip,
	@SerializedName("careContexts") val careReferences : List<CareReference>,
	@SerializedName("permission") val permission : Permission
)