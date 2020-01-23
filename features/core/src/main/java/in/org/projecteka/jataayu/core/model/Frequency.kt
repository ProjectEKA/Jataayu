package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Frequency (

	@SerializedName("unit") val id : String,
	@SerializedName("value") val code : Integer,
	@SerializedName("repeats") val repeats : Integer
)