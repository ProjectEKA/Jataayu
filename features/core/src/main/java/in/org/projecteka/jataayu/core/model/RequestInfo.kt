package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class RequestInfo (
	@SerializedName("infoFrom") val infoFrom : String,
	@SerializedName("infoTo") val infoTo : String,
	@SerializedName("infoTypes") val infoTypes : List<InfoTypes>)
