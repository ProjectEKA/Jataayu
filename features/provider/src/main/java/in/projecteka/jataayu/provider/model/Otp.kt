package `in`.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class Otp(@SerializedName("token") val token: String)