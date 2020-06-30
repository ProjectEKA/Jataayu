package model

import com.google.gson.annotations.SerializedName

data class CreateAccountResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("expiresIn") val accessTokenExpiresIn: Long,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("refreshExpiresIn") val refreshExpiresIn: Long,
    @SerializedName("tokenType") val tokenType: String
)

