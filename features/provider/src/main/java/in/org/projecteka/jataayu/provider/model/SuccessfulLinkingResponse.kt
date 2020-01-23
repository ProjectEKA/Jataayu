package `in`.org.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class SuccessfulLinkingResponse (
	@SerializedName("patient") val patient : Patient
)