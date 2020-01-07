package `in`.org.projecteka.jataayu.consent.model

import `in`.org.projecteka.jataayu.core.model.Consent
import com.google.gson.annotations.SerializedName

data class ConsentsListResponse (
	@SerializedName("requests") val requests : List<Consent>
)