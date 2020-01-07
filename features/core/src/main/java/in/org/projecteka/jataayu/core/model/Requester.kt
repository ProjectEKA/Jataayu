package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Requester (

	@SerializedName("name") val name : String,
	@SerializedName("identifier") val identifier : Identifier
)