package `in`.org.projecteka.jataayu.consent.model

import `in`.org.projecteka.jataayu.core.model.Links
import com.google.gson.annotations.SerializedName

data class AssociatedAccountsResponse (
	@SerializedName("links") val links : List<Links>
)