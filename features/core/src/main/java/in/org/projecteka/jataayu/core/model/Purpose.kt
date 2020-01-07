package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Purpose (
	@SerializedName("text") val text : String,
	@SerializedName("code") val code : String,
	@SerializedName("refUri") val refUri : String
)