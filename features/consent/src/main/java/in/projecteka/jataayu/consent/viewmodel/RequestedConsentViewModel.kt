package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.helper.ConsentDataHelper
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.RequestStatus.DENIED
import `in`.projecteka.jataayu.core.model.RequestStatus.REQUESTED
import `in`.projecteka.jataayu.core.model.approveconsent.CareReference
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifact
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.LinkedHip
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RequestedConsentViewModel(private val repository: ConsentRepository) : ViewModel() {

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()
    val linkedAccountsResponse = PayloadLiveData<LinkedAccountsResponse>()
    val consentArtifactResponse = PayloadLiveData<ConsentArtifactResponse>()
    val consentDenyResponse = PayloadLiveData<Void>()
    val grantedConsentDetailsResponse = PayloadLiveData<List<GrantedConsentDetailsResponse>>()

    val requestedConsentsList = MutableLiveData<List<Consent>>()

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

    fun grantConsent(
        requestId: String,
        consentArtifacts: List<ConsentArtifact>,
        authToken: String
    ) =
        consentArtifactResponse.fetch(repository.grantConsent(requestId, ConsentArtifactRequest((consentArtifacts)), authToken))


    fun getConsentArtifact(
        links: List<Links>,
        hiTypeObjects: ArrayList<HiType>,
        permission: Permission
    ): List<ConsentArtifact> {

        val consentArtifactList = ArrayList<ConsentArtifact>()
        val hiTypes: List<String> = hiTypeObjects.mapNotNull { if(it.isChecked) it.type else null }


        links.forEach { link ->
            val careReferences = ArrayList<CareReference>()
            link.careContexts.forEach { careContext ->
                if (careContext.contextChecked) careReferences.add(newCareReference(link, careContext))
            }

            if (careReferences.isNotEmpty()) {
                consentArtifactList.add(
                    ConsentArtifact(hiTypes, link.hip, careReferences, permission)
                )
            }
        }
        return consentArtifactList
    }

    fun denyConsent(requestId: String){
        consentDenyResponse.fetch(repository.denyConsent(requestId))
    }

    fun populateFilterItems(resources: Resources, flow: ConsentFlow?): List<String> =
        requestedConsentStatusList.map { getFormattedItem(resources,it, REQUESTED)  }


    private fun getFormattedItem(
        resources: Resources,
        filterItem: Int,
        requestStatus: RequestStatus
    ): String {
        val list = requestedConsentsList.value

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

    private fun newCareReference(link: Links, it: CareContext) = CareReference(link.referenceNumber, it.referenceNumber)

    fun checkSelectionInBackground(listOfBindingModels: List<IDataBindingModel>?): Pair<Boolean, Boolean> {
        var selectableItemsCount = 0
        var selectionCount = 0

        listOfBindingModels?.filterIsInstance<CareContext>()?.run {
            selectableItemsCount = count()
            selectionCount = count { it.contextChecked }
        }
        return Pair(selectableItemsCount == selectionCount, selectionCount > 0)
    }

    fun filterConsents(consentList: List<Consent>?) {
        val filteredConsents = consentList?.filter {
            it.status == REQUESTED || it.status == DENIED
        }
        if (filteredConsents != null) {
            requestedConsentsList.value = ConsentDataHelper.sortConsentListByLastUpdatedDate(filteredConsents)
        } else {
            requestedConsentsList.value = filteredConsents
        }

    }

    fun getItems(grantedConsents: List<GrantedConsentDetailsResponse>, linkedAccounts: List<Links>?): Pair<List<IDataBindingModel>,Int> {
        var count = 0
        val items = arrayListOf<IDataBindingModel>()
        for (grantedConsent in grantedConsents) {
            val grantedAccountHipId = grantedConsent.consentDetail.hip?.id
            linkedAccounts?.forEach { link ->
                if (grantedAccountHipId == link.hip.id) {
                    val linkedHip = LinkedHip(link.hip.name, link.referenceNumber)
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

