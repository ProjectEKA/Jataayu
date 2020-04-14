package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class CreateAccountRequest(
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("yearOfBirth") val yearOfBirth: Int?
)