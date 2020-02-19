package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class CreateAccountRequest(
    @SerializedName("user_name") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("dob") val dob: String
)