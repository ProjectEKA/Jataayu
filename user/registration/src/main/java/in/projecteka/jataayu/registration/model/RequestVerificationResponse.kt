package `in`.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class RequestVerificationResponse(@SerializedName("sessionId") val sessionId: String)