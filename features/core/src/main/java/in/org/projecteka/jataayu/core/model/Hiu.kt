package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Hiu (

	@SerializedName("name") val name : String,
	@SerializedName("organization") val organization : ProviderInfo
)