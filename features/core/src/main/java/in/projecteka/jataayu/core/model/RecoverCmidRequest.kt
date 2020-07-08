package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class RecoverCmidRequest(
    @SerializedName("name") val name: Name,
    @SerializedName("gender") val gender: String,
    @SerializedName("dateOfBirth") val dateOfBirth: DateOfBirth?,
    @SerializedName("verifiedIdentifiers") val verifiedIdentifiers: List<Identifier>?,
    @SerializedName("unverifiedIdentifiers") val unverifiedIdentifiers: List<UnverifiedIdentifier>?
)