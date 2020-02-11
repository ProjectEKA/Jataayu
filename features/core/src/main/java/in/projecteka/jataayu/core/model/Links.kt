package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Links (
	@SerializedName("hip") val hip : Hip,
	@SerializedName("referenceNumber") val referenceNumber: String?,
	@SerializedName("display") val display: String?,
	@SerializedName("careContexts") val careContexts : List<CareContext>
)