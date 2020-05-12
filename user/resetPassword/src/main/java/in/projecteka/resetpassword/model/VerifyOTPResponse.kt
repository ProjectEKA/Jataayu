package `in`.projecteka.resetpassword.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPResponse(@SerializedName("temporaryToken") val temporaryToken: String)