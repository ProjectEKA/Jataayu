package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class HiuRequester (

	@SerializedName("id") private val id : String,
	@SerializedName("name") var name : String
): HipHiuIdentifiable {
	override fun getId(): String {
		return id
	}
}