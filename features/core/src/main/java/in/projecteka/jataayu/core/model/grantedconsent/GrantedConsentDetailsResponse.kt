package `in`.projecteka.jataayu.core.model.grantedconsent

import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.RequestStatus
import com.google.gson.annotations.SerializedName

data class GrantedConsentDetailsResponse(
	@SerializedName("status") val status : RequestStatus,
	@SerializedName("consentDetail") val consentDetail : Consent,
	@SerializedName("signature") val signature : String


)