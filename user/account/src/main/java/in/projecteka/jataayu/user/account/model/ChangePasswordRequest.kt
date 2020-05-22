package `in`.projecteka.jataayu.user.account.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("oldPassword") private val oldPassword: String?,
    @SerializedName("newPassword") private val newPassword: String?
)
