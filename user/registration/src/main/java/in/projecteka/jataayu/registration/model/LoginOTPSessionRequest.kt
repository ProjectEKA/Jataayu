package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class LoginOTPSessionRequest(
    @SerializedName("username") val userName: String
)