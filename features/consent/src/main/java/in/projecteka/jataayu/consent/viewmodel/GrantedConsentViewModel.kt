package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.Cache.ConsentDataProviderCacheManager
import `in`.projecteka.jataayu.consent.extension.grantedConsentList
import `in`.projecteka.jataayu.consent.extension.requestedConsentList
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.RequestStatus.*
import `in`.projecteka.jataayu.core.model.approveconsent.CareReference
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifact
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.LinkedHip
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GrantedConsentViewModel(private val repository: ConsentRepository,
                              private val credentialsRepository: CredentialsRepository) : BaseViewModel() {

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()
    val linkedAccountsResponse = PayloadLiveData<LinkedAccountsResponse>()
    val grantedConsentDetailsResponse = PayloadLiveData<List<GrantedConsentDetailsResponse>>()
    val revokeConsentResponse = PayloadLiveData<Void>()

    val requestedConsentsList = MutableLiveData<List<Consent>>()
    val grantedConsentsList = MutableLiveData<List<Consent>>()


    private val grantedConsentStatusList = listOf(
        R.string.status_all_granted_consents,
        R.string.status_active_granted_consents,
        R.string.status_expired_granted_consents
    )
    private val requestedConsentStatusList = listOf(
        R.string.status_active_requested_consents,
        R.string.status_expired_requested_consents,
        R.string.status_denied_consent_requests,
        R.string.status_all_requested_consents
    )

    internal var selectedProviderName = String.EMPTY

    fun getConsents() =
        consentListResponse.fetch(repository.getConsents())


    fun getLinkedAccounts() =
        linkedAccountsResponse.fetch(repository.getLinkedAccounts())


    fun getGrantedConsentDetails(requestId: String) {
        grantedConsentDetailsResponse.fetch(repository.getGrantedConsentDetails(requestId))
    }

    fun getConsentRepository(): ConsentRepository = repository

    fun populateFilterItems(resources: Resources, flow: ConsentFlow?): List<String> =
        if (flow == ConsentFlow.GRANTED_CONSENTS) {
            grantedConsentStatusList.map { getFormattedItem(resources, it, GRANTED) }
        } else {
            requestedConsentStatusList.map {
                getFormattedItem(resources, it, REQUESTED)
            }
        }


    private fun getFormattedItem(
        resources: Resources,
        filterItem: Int,
        requestStatus: RequestStatus
    ): String {
        val list = if (requestStatus == GRANTED) {
            grantedConsentsList.value
        } else {
            requestedConsentsList.value
        }

        val count = list?.count { consent ->
            val dataExpired = DateTimeUtils.isDateExpired(consent.permission.dataEraseAt)
            when (filterItem) {
                R.string.status_denied_consent_requests -> {
                    consent.status == DENIED
                }
                R.string.status_active_granted_consents,
                R.string.status_active_requested_consents -> {
                    if (consent.status != DENIED) !dataExpired else false
                }
                R.string.status_expired_granted_consents,
                R.string.status_expired_requested_consents -> {
                    if (consent.status != DENIED) dataExpired else false
                }
                else -> true
            }
        }

        return String.format(resources.getString(filterItem), count)
    }

    fun filterConsents(consentList: List<Consent>?) {

        requestedConsentsList.value = consentList?.requestedConsentList()
        grantedConsentsList.value = consentList?.grantedConsentList()
    }

    fun revokeConsent(consentArtifactId: String) {
        val list: ArrayList<String> = ArrayList()
        list.add(consentArtifactId)
        revokeConsentResponse.fetch(repository.revokeConsent(RevokeConsentRequest(list), credentialsRepository.consentTemporaryToken))
    }

    fun getItems(grantedConsents: List<GrantedConsentDetailsResponse>, linkedAccounts: List<Links>?): Pair<List<IDataBindingModel>, Int> {
        var count = 0
        val items = arrayListOf<IDataBindingModel>()
        for (grantedConsent in grantedConsents) {
            val grantedAccountHipId = grantedConsent.consentDetail.hip?.id
            linkedAccounts?.forEach { link ->
                if (grantedAccountHipId == link.hip.id) {
                    // As per the requirement get the HIP name from ID
                    val linkedHipName = ConsentDataProviderCacheManager.providerMap[grantedAccountHipId]?.hip?.name ?: ""
                    val linkedHip = LinkedHip(linkedHipName, link.referenceNumber)
                    items.add(linkedHip)
                    count++
                    val careContextsList = arrayListOf<LinkedCareContext>()
                    link.careContexts.forEach { careContextsList.add(LinkedCareContext(it.referenceNumber, it.display)) }
                    (careContextsList).forEach { linkedAccountCareContext ->
                        (grantedConsent.consentDetail.careContexts)?.forEach { grantedAccountCareContext ->
                            grantedAccountCareContext.careContextReference.toString()
                            if (linkedAccountCareContext.referenceNumber == grantedAccountCareContext.careContextReference) {
                                items.add(linkedAccountCareContext)
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
        return Pair(items, count)
    }
}

