package `in`.projecteka.jataayu.consent.model

import `in`.projecteka.jataayu.core.model.Consent
import com.google.gson.annotations.SerializedName

data class ConsentsListResponse (
	@SerializedName("requests") val requests : List<Consent>,
	@SerializedName( "size") val totalCount: Int,
	@SerializedName("offset") val offset: Int
)