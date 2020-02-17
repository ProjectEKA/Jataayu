package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class VerifyIdentifierResponse(@SerializedName("temporaryToken") val token: String)