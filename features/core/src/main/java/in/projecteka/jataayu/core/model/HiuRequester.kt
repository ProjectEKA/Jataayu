package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class HiuRequester (

	@SerializedName("id") val id : String,
	@SerializedName("name") var name : String
)