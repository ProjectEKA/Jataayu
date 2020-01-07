package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class HiTypes (

	@SerializedName("type") val type : String,
	@SerializedName("description") val description : String
)