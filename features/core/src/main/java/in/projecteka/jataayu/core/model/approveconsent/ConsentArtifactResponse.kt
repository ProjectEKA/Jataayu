package `in`.projecteka.jataayu.core.model.approveconsent

import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import com.google.gson.annotations.SerializedName

data class ConsentArtifactResponse(

    @SerializedName("consents") val consents: List<ApprovedConsent>?,
    @SerializedName("consentArtefacts")
    private val consentArtefacts: List<GrantedConsentDetailsResponse>?,
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("signature")
    val signature: String?,
    @SerializedName("size")
    val size: Int = 0
) {

    fun getArtifacts(): List<Consent> {
        return consentArtefacts?.map {
            it.consentDetail.status = it.status
            it.consentDetail
        } ?: listOf()
    }

}


data class ConsentManager(
    @SerializedName("id")
    val id: String
)





