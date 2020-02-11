package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class InfoTypes (

	@SerializedName("id") val id : String,
	@SerializedName("type") val type : String
)