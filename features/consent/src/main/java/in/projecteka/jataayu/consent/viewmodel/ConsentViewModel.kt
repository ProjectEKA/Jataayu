package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.RequestStatus.GRANTED
import `in`.projecteka.jataayu.core.model.RequestStatus.REQUESTED
import `in`.projecteka.jataayu.core.model.approveconsent.CareReference
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifact
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.observeOn
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.extension.liveDataOf
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsentViewModel(private val repository: ConsentRepository) : ViewModel() {
    val consentsListResponse = liveDataOf<ConsentsListResponse>()
    var requestedConsentsList = MutableLiveData<List<Consent>>()
    var grantedConsentsList = MutableLiveData<List<Consent>>()
    private val grantedConsentStatusList = listOf(R.string.status_active_granted_consents,R.string.status_expired_granted_consents, R.string.status_all_granted_consents)
    private val requestedConsentStatusList = listOf(R.string.status_active_requested_consents, R.string.status_expired_requested_consents, R.string.status_all_requested_consents)

    internal var selectedProviderName = String.EMPTY

    var requests = emptyList<Consent>()

    val linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()

    val consentArtifactResponse = liveDataOf<ConsentArtifactResponse>()

    fun getConsents(responseCallback: ResponseCallback) {
        repository.getConsents().observeOn(consentsListResponse, responseCallback)
    }

    fun getLinkedAccounts(responseCallback: ResponseCallback) {
        repository.getLinkedAccounts().observeOn(linkedAccountsResponse, responseCallback)
    }

    fun grantConsent(
        requestId: String,
        consentArtifacts: List<ConsentArtifact>,
        responseCallback: ResponseCallback
    ) {
        repository.grantConsent(requestId, ConsentArtifactRequest(consentArtifacts))
            .observeOn(consentArtifactResponse, responseCallback)
    }

    fun isRequestAvailable(): Boolean {
        return consentsListResponse.value?.requests!!.isNotEmpty()
    }

    fun getConsentArtifact(
        links: List<Links?>,
        hiTypeObjects: ArrayList<HiType>,
        permission: Permission
    ): List<ConsentArtifact> {
        val consentArtifactList = ArrayList<ConsentArtifact>()

        val hiTypes = ArrayList<String>()
        hiTypeObjects.forEach { hiTypes.add(it.type) }

        links.forEach { link ->
            val careReferences = ArrayList<CareReference>()
            link!!.careContexts.forEach { careContext ->
                if (careContext.contextChecked) careReferences.add(
                    newCareReference(
                        link,
                        careContext
                    )
                )
            }

            if (careReferences.isNotEmpty()) {
                consentArtifactList.add(
                    ConsentArtifact(
                        hiTypes,
                        link.hip,
                        careReferences,
                        permission
                    )
                )
            }
        }
        return consentArtifactList
    }

    fun populateFilterItems(resources: Resources, flow: ConsentFlow?): List<String> {
        var items = mutableListOf<String>()
        if (flow == ConsentFlow.GRANTED_CONSENTS) {
            grantedConsentStatusList.forEach { items.add(getFormattedItem(resources, it, GRANTED)) }
        } else {
            requestedConsentStatusList.forEach { items.add(getFormattedItem(resources, it, REQUESTED)) }
        }
        return items
    }

    private fun getFormattedItem(
        resources: Resources,
        filterItem: Int,
        requestStatus: RequestStatus
    ):
            String {
        var count = 0


        val list = if(requestStatus == GRANTED){
            grantedConsentsList.value
        } else{
            requestedConsentsList.value
        }

        list.let {
            it?.forEach { consent ->
                val dataExpired = DateTimeUtils.isDateExpired(consent.permission.dataExpiryAt)
                when (filterItem) {
                    R.string.status_active_granted_consents, R.string.status_active_requested_consents -> {
                        if (!dataExpired) count++
                    }
                    R.string.status_expired_granted_consents, R.string.status_expired_requested_consents -> {
                        if (dataExpired) count++
                    }
                    else -> {
                        count++
                    }
                }
            }
        }

        return String.format(resources.getString(filterItem), count)
    }

    fun getItems(links: List<Links?>): List<IDataBindingModel> {
        val items = arrayListOf<IDataBindingModel>()
        links.forEach { link ->
            items.add(link?.hip!!)
            items.addAll(link.careContexts)
        }
        return items
    }

    private fun newCareReference(
        link: Links,
        it: CareContext
    ) = CareReference(link.referenceNumber!!, it.referenceNumber)

    fun checkSelectionInBackground(listOfBindingModels: List<IDataBindingModel>?): Pair<Boolean, Boolean> {
        var selectionCount = 0
        var selectableItemsCount = 0

        listOfBindingModels?.forEach {
            if (it is CareContext) {
                selectableItemsCount++
                if (it.contextChecked) selectionCount++
            }
        }
        return Pair(selectableItemsCount == selectionCount, selectionCount > 0)
    }

    fun filterConsents() {
        requestedConsentsList.value = consentsListResponse.value?.requests!!.filter {
            it.status == REQUESTED
        }
        grantedConsentsList.value = consentsListResponse.value?.requests!!.filter {
            it.status == GRANTED
        }
    }
}

