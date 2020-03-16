package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class MyProfile(
    @SerializedName("id") val id: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("hasTransactionPin") val hasTransactionPin: Boolean
    )