package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("userName") val userName: String ,
    @SerializedName("password") private val password: String,
    @SerializedName("grantType") private val grantType: String
)