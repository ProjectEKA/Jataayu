package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class MyProfile(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("hasTransactionPin") val hasTransactionPin: Boolean,
    @SerializedName("verifiedIdentifiers") val verifiedIdentifiers: List<Identifier>
    )