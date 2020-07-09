package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class CreateAccountRequest(
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: Name,
    @SerializedName("gender") val gender: String,
    @SerializedName("dateOfBirth") val dateOfBirth: DateOfBirth?,
    @SerializedName("unverifiedIdentifiers") val unverifiedIdentifiers: List<UnverifiedIdentifier>?
)