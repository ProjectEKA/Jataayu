package `in`.projecteka.resetpassword.model

import com.google.gson.annotations.SerializedName

data class GenerateOTPRequest(
    @SerializedName("username") private val consentManagerId: String?
)
