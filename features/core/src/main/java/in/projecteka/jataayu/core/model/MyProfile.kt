package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class MyProfile(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: Name,
    @SerializedName("gender") val gender: String,
    @SerializedName("dateOfBirth") val dateOfBirth: DateOfBirth?,
    @SerializedName("hasTransactionPin") val hasTransactionPin: Boolean,
    @SerializedName("verifiedIdentifiers") val verifiedIdentifiers: List<Identifier>?,
    @SerializedName("unverifiedIdentifiers") val unverifiedIdentifiers: List<UnverifiedIdentifier>?
    )