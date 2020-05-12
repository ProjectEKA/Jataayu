package `in`.projecteka.jataayu.core.model.approveconsent

import `in`.projecteka.jataayu.core.model.*
import com.google.gson.annotations.SerializedName

data class ConsentArtifactResponse(

    @SerializedName("consents") val consents: List<ApprovedConsent>?,
    @SerializedName("consentArtefacts")
    val consentArtefacts: List<ConsentArtifact>?,
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("signature")
    val signature: String?,
    @SerializedName("size")
    val size: Int = 0
)


data class ConsentDetail(
    @SerializedName("careContexts")
    val careContexts: List<CareContext>,
    @SerializedName("consentId")
    val consentId: String,
    @SerializedName("consentManager")
    val consentManager: ConsentManager,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("hiTypes")
    val hiTypes: List<String>,
    @SerializedName("hip")
    val hip: Hip,
    @SerializedName("hiu")
    val hiu: Hiu,
    @SerializedName("patient")
    val patient: Patient,
    @SerializedName("permission")
    val permission: Permission,
    @SerializedName("purpose")
    val purpose: Purpose,
    @SerializedName("requester")
    val requester: Requester,
    @SerializedName("schemaVersion")
    val schemaVersion: String
)


data class ConsentManager(
    @SerializedName("id")
    val id: String
)



