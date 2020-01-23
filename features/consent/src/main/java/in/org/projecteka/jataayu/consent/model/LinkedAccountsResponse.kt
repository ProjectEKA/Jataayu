package `in`.org.projecteka.jataayu.consent.model

import com.google.gson.annotations.SerializedName

data class LinkedAccountsResponse (
	@SerializedName("links") val links : List<Links>
)