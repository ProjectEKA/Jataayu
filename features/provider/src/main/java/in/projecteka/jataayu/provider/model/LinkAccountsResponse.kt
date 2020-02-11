package `in`.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class LinkAccountsResponse (
	@SerializedName("transactionId") val transactionId : String,
	@SerializedName("link") val link : Link
)