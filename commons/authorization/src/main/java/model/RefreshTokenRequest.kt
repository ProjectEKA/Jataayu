package model

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest (
    @SerializedName("username") val userName: String ,
    @SerializedName("grantType") private val grantType: String,
    @SerializedName("refresh_token") private val refreshToken: String
)