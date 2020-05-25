package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class RecoverCmidRequest(
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("yearOfBirth") val yearOfBirth: Int?,
    @SerializedName("verifiedIdentifiers") val verifiedIdentifiers: List<Identifier>?,
    @SerializedName("unverifiedIdentifiers") val unverifiedIdentifiers: List<UnverifiedIdentifier>?
)