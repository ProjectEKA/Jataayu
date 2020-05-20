package `in`.projecteka.jataayu.provider.viewmodel

import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.provider.model.*
import `in`.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.extension.liveDataOf
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.UUIDRepository
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class ProviderSearchViewModel(private val providerRepository: ProviderRepository,
                              private val userAccountsRepository: UserAccountsRepository,
                              val preferenceRepository: PreferenceRepository,
                              val uuidRepository: UUIDRepository) : BaseViewModel(), TextWatcher {
    val providers = liveDataOf<List<ProviderInfo>>()
    var providersList = emptyList<ProviderInfo>()
    val patientDiscoveryResponse = liveDataOf<PatientDiscoveryResponse>()
    val linkAccountsResponse = liveDataOf<LinkAccountsResponse>()
    val successfulLinkingResponse = liveDataOf<SuccessfulLinkingResponse>()
    val userProfileResponse = MediatorLiveData<PayloadResource<*>>()
    val myProfile = SingleLiveEvent<MyProfile>()
    val isViewDetailsEnabled = ObservableBoolean(false)


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

    fun getMyProfile() = userAccountsRepository.getMyProfile()

    fun linkPatientAccounts(listCareContexts: List<CareContext>, responseCallback: ResponseCallback) {

        var linkedAccounts = ArrayList<CareContext>()

        listCareContexts.forEach {
                if (it.contextChecked){
                linkedAccounts.add(it)
            }
        }

        val discoveryResponse = patientDiscoveryResponse.value
        val selectedAccountsResponse = LinkPatientAccountRequest(
            uuidRepository.generateUUID(), Patient(discoveryResponse?.patient?.referenceNumber!!,
            discoveryResponse.patient.display, linkedAccounts, discoveryResponse.patient?.matchedBy!!),
        discoveryResponse.transactionId)

        providerRepository.linkPatientAccounts(selectedAccountsResponse!!).observeOn(linkAccountsResponse, responseCallback)
    }

    fun fetchProfileData() {
        val profileLiveData = getMyProfile()
        userProfileResponse.addSource(profileLiveData, Observer {
            when (it) {
                is Success -> {
                    myProfile.value = it.data
                }
                is PartialFailure -> {
                }
            }
        })
    }

    fun verifyOtp(referenceNumber: String, otp: Otp, responseCallback: ResponseCallback) {
        providerRepository.verifyOtp(referenceNumber, otp).observeOn(successfulLinkingResponse, responseCallback)
    }

    fun canLinkAccounts(careContexts: List<CareContext>): Pair<Boolean, Int> {
        val checkedCareContexts = careContexts.filter { it.contextChecked == true }
        return Pair(checkedCareContexts.isNotEmpty(), checkedCareContexts.count())
    }

    fun clearList() {
        providersList = emptyList()
    }

    fun makeAccountsSelected()  {
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

