package `in`.projecteka.resetpassword.model

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("password") private val password: String?
)
