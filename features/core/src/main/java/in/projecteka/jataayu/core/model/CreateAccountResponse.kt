package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class CreateAccountResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("refresh_expires_in") val refreshExpiresIn: Int,
    @SerializedName("token_type") val tokenType: String
)