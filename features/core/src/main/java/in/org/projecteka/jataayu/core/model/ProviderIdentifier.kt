package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class ProviderIdentifier(@SerializedName("id") val id: String, @SerializedName("name") val name: String)