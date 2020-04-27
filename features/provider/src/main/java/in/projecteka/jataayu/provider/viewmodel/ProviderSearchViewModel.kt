package `in`.projecteka.jataayu.provider.viewmodel

import `in`.projecteka.jataayu.core.model.CareContext
import `in`.projecteka.jataayu.core.model.Patient
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.observeOn
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.projecteka.jataayu.provider.model.Otp
import `in`.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.extension.liveDataOf
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class ProviderSearchViewModel(private val providerRepository: ProviderRepository,
                              val preferenceRepository: PreferenceRepository) : BaseViewModel(), TextWatcher {
    val providers = liveDataOf<List<ProviderInfo>>()
    var providersList = emptyList<ProviderInfo>()
    val patientDiscoveryResponse = liveDataOf<PatientDiscoveryResponse>()
    val linkAccountsResponse = liveDataOf<LinkAccountsResponse>()
    val successfulLinkingResponse = liveDataOf<SuccessfulLinkingResponse>()

    internal var selectedProviderName = String.EMPTY

    val otpText = ObservableField<String>()
    val errorLbl = ObservableField<String>()
    val setEnableButton = ObservableBoolean()

    companion object {
        const val OTP_LENGTH = 6
    }

    fun getProviders(query: String) {
        if (providersList.isEmpty()) providerRepository.getProviders(query).observeOn(providers)
        else providers.postValue(providersList.filter { it.hip.name.contains(query, true) })
    }

    fun getPatientAccounts(request: Request, responseCallback: ResponseCallback) {
        providerRepository.getPatientAccounts(request).observeOn(patientDiscoveryResponse, responseCallback)
    }

    fun linkPatientAccounts(listCareContexts: List<CareContext>, responseCallback: ResponseCallback) {

        var linkedAccounts = ArrayList<CareContext>()

        listCareContexts.forEach {
                if (it.contextChecked){
                linkedAccounts.add(it)
            }
        }

        val discoveryResponse = patientDiscoveryResponse.value
        val selectedAccountsResponse = PatientDiscoveryResponse(Patient(discoveryResponse?.patient?.referenceNumber!!,
            discoveryResponse.patient.display, linkedAccounts, discoveryResponse.patient?.matchedBy!!),
            discoveryResponse.transactionId)


        providerRepository.linkPatientAccounts(selectedAccountsResponse!!).observeOn(linkAccountsResponse, responseCallback)
    }

    fun verifyOtp(referenceNumber: String, otp: Otp, responseCallback: ResponseCallback) {
        providerRepository.verifyOtp(referenceNumber, otp).observeOn(successfulLinkingResponse, responseCallback)
    }

    fun canLinkAccounts(careContexts: List<CareContext>): Boolean {
        for (careContext in careContexts) {
            if (careContext.contextChecked) return true
        }
        return false
    }

    fun clearList() {
        providersList = emptyList()
    }

    fun makeAccountsSelected() {
        patientDiscoveryResponse.value?.patient?.careContexts!!.forEach { careContext -> careContext.contextChecked = true }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (otpText.get()?.isNotEmpty() == true) {
            errorLbl.set("")
        }

        setEnableButton.set(s?.length == OTP_LENGTH)
    }
}

