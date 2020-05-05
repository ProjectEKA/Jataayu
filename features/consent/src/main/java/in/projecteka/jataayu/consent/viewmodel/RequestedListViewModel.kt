package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.extension.requestedConsentList
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.HipHiuNameResponse
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.RequestStatus.DENIED
import `in`.projecteka.jataayu.core.model.RequestStatus.REQUESTED
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class RequestedListViewModel(private val repository: ConsentRepository) : BaseViewModel() {

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()
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
                R.string.status_active_requested_consents -> {
                    if (consent.status != DENIED) !dataExpired else false
                }
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
    }

    fun fetchHipHiuNamesOf(idList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        return repository.getProviderBy(idList)
    }
}

