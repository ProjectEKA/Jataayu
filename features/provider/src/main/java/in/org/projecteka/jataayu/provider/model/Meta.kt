package `in`.org.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class Meta (
	@SerializedName("communicationMedium") val communicationMedium : String,
	@SerializedName("communicationHint") val communicationHint : String,
	@SerializedName("communicationExpiry") val communicationExpiry : String
)