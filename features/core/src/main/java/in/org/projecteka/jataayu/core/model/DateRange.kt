package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class DateRange (

	@SerializedName("from") val from : String,
	@SerializedName("to") val to : String
)