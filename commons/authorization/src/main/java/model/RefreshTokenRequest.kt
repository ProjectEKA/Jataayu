package model

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest (
    @SerializedName("username") val userName: String ,
    @SerializedName("grantType") private val grantType: String,
    @SerializedName("refreshToken") private val refreshToken: String
)