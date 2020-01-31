package `in`.org.projecteka.jataayu.registration.model

import com.google.gson.annotations.SerializedName

data class RequestVerificationResponse(@SerializedName("sessionId") val sessionId: String)