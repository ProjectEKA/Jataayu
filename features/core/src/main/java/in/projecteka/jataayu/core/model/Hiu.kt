package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Hiu (

	@SerializedName("name") val name : String,
	@SerializedName("organization") val organization : ProviderInfo
): HipHiuIdentifiable {

	override fun getId(): String {
		return organization.hip.getId()
	}
}