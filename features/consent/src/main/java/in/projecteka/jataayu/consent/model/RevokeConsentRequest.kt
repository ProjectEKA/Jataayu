package `in`.projecteka.jataayu.consent.model

import com.google.gson.annotations.SerializedName

data class RevokeConsentRequest(@SerializedName("consents") val consentId: ArrayList<String>)