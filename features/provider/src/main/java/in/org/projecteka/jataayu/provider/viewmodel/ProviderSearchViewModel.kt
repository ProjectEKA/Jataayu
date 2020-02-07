package `in`.org.projecteka.jataayu.provider.viewmodel

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.core.model.Request
import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.network.utils.observeOn
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.org.projecteka.jataayu.provider.model.Token
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel

class ProviderSearchViewModel(private val providerRepository: ProviderRepository) : ViewModel() {
    val mobile = "9876543210"
    val providers = liveDataOf<List<ProviderInfo>>()
    var providersList = emptyList<ProviderInfo>()
    val patientDiscoveryResponse = liveDataOf<PatientDiscoveryResponse>()
    val linkAccountsResponse = liveDataOf<LinkAccountsResponse>()
    val successfulLinkingResponse = liveDataOf<SuccessfulLinkingResponse>()

    internal var selectedProviderName = String.EMPTY

    fun getProviders(query: String) {
        if (providersList.isEmpty()) providerRepository.getProviders(query).observeOn(providers)
        else providers.postValue(providersList.filter { it.hip.name.contains(query, true) })
    }

    fun getPatientAccounts(request: Request, responseCallback: ResponseCallback) {
        providerRepository.getPatientAccounts(request).observeOn(patientDiscoveryResponse, responseCallback)
    }

    fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse, responseCallback: ResponseCallback) {
        providerRepository.linkPatientAccounts(patientDiscoveryResponse)
            .observeOn(linkAccountsResponse, responseCallback)
    }

    fun verifyOtp(referenceNumber: String, token: Token, responseCallback: ResponseCallback) {
        providerRepository.verifyOtp(referenceNumber, token).observeOn(successfulLinkingResponse, responseCallback)
    }

    fun canLinkAccounts(): Boolean {
        for (careContext in patientDiscoveryResponse.value?.patient?.careContexts!!) {
            if (careContext.contextChecked) return true
        }
        return false
    }

    fun clearList() {
        providersList = emptyList()
    }
}

