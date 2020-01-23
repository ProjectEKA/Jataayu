package `in`.org.projecteka.jataayu.provider.model

import `in`.org.projecteka.jataayu.core.model.Patient
import com.google.gson.annotations.SerializedName

data class SuccessfulLinkingResponse (
	@SerializedName("patient") val patient : Patient
)