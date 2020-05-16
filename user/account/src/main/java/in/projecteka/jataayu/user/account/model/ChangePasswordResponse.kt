package `in`.projecteka.jataayu.user.account.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("expiresIn") val expiresIn: Int,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("refreshExpiresIn") val refreshExpiresIn: Int,
    @SerializedName("tokenType") val tokenType: String
)