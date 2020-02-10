package `in`.org.projecteka.jataayu.consent.viewmodel

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.core.model.*
import `in`.org.projecteka.jataayu.core.model.approveconsent.CareReference
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifact
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.network.utils.observeOn
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import android.content.res.Resources
import androidx.lifecycle.ViewModel

class ConsentViewModel(private val repository: ConsentRepository) : ViewModel() {
    val consentsListResponse = liveDataOf<ConsentsListResponse>()

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

    fun grantConsent(requestId: String, consentArtifacts: List<ConsentArtifact>, responseCallback: ResponseCallback) {
        repository.grantConsent(requestId, ConsentArtifactRequest(consentArtifacts))
            .observeOn(consentArtifactResponse, responseCallback)
    }

    fun isRequestAvailable(): Boolean {
        return consentsListResponse.value?.requests!!.isNotEmpty()
    }

    fun getConsentArtifact(links: List<Links?>, hiTypeObjects: ArrayList<HiType>, permission: Permission): List<ConsentArtifact> {
        val consentArtifactList =  ArrayList<ConsentArtifact>()

        val hiTypes = ArrayList<String>()
        hiTypeObjects.forEach { hiTypes.add(it.type) }

        links.forEach {link ->
            val careReferences = ArrayList<CareReference>()
            link!!.careContexts.forEach {careContext ->
                if (careContext.contextChecked) careReferences.add(newCareReference(link, careContext))
            }

            if (careReferences.isNotEmpty()) {
                consentArtifactList.add(ConsentArtifact(hiTypes, link.hip, careReferences, permission))
            }
        }
        return consentArtifactList
    }

    fun populateFilterItems(resources: Resources): List<String> {
        val items = ArrayList<String>(3)
        items.add(String.format(resources.getString(R.string.status_all_requests), requests.size))
        items.add(getFormattedItem(resources.getString(R.string.status_active_requests), RequestStatus.REQUESTED))
        items.add(getFormattedItem(resources.getString(R.string.status_expired_requests), RequestStatus.EXPIRED))
        return items
    }

    private fun getFormattedItem(filterItem: String, requestStatus: RequestStatus): String {
        var count = 0
        requests.forEach { if (requestStatus == it.status) count++ }
        return String.format(filterItem, count)
    }

    fun getItems(links: List<Links?>): List<IDataBindingModel> {
        val items = arrayListOf<IDataBindingModel>()
        links.forEach {link ->
            items.add(link?.hip!!)
            items.addAll(link.careContexts)
        }
        return items
    }

    private fun newCareReference(
        link: Links,
        it: CareContext
    ) = CareReference(link.referenceNumber!!, it.referenceNumber)

    fun checkSelectionInBackground(listOfBindingModels: List<IDataBindingModel>?) : Pair<Boolean, Boolean> {
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
}

