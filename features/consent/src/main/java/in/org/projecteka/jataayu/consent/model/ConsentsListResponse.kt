package `in`.org.projecteka.jataayu.consent.model

import `in`.org.projecteka.jataayu.core.model.ConsentModel
import com.google.gson.annotations.SerializedName

data class ConsentsListResponse (
	@SerializedName("consents") val consents : List<ConsentModel>
)