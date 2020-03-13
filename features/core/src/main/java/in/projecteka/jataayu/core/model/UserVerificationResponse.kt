package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class UserVerificationResponse(@SerializedName("temporaryToken") val temporaryToken : String)