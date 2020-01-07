package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Permission (

	@SerializedName("accessMode") val accessMode : String,
	@SerializedName("dateRange") val dateRange : DateRange,
	@SerializedName("dataExpiryAt") val dataExpiryAt : String,
	@SerializedName("frequency") val frequency : Frequency
)