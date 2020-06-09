package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val userName: String ,
    @SerializedName("password") private val password: String,
    @SerializedName("grantType") private val grantType: String
)