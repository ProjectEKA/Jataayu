package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.extension.grantedConsentList
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.HipHiuNameResponse
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.RequestStatus.DENIED
import `in`.projecteka.jataayu.core.model.RequestStatus.GRANTED
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GrantedConsentListViewModel(private val repository: ConsentRepository) : ViewModel() {

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()
    val grantedConsentDetailsResponse = PayloadLiveData<List<GrantedConsentDetailsResponse>>()
    val revokeConsentResponse = PayloadLiveData<Void>()
    val grantedConsentsList = MutableLiveData<List<Consent>>()


    private val grantedConsentStatusList = listOf(
        R.string.status_all_granted_consents,
        R.string.status_active_granted_consents,
        R.string.status_expired_granted_consents
    )

    internal var selectedProviderName = String.EMPTY

    fun getConsents() =
        consentListResponse.fetch(repository.getConsents())

    fun getGrantedConsentDetails(requestId: String) {
        grantedConsentDetailsResponse.fetch(repository.getGrantedConsentDetails(requestId))
    }


    fun populateFilterItems(resources: Resources, flow: ConsentFlow?): List<String> =
        grantedConsentStatusList.map { getFormattedItem(resources,it, GRANTED) }


    private fun getFormattedItem(
        resources: Resources,
        filterItem: Int,
        requestStatus: RequestStatus
    ): String {
        val list = grantedConsentsList.value

        val count = list?.count { consent ->
            val dataExpired = DateTimeUtils.isDateExpired(consent.permission.dataEraseAt)
            when (filterItem) {
                R.string.status_active_granted_consents -> {
                    if (consent.status != DENIED) !dataExpired else false
                }
                R.string.status_expired_granted_consents -> {
                    if (consent.status != DENIED) dataExpired else false
                }
                else -> true
            }
        }

        return String.format(resources.getString(filterItem), count)
    }

    fun filterConsents(consentList: List<Consent>?) {
        grantedConsentsList.value = consentList?.grantedConsentList()
    }

    fun revokeConsent(consentArtifactId: String, authToken: String) {
        val list: ArrayList<String> = ArrayList()
        list.add(consentArtifactId)
        revokeConsentResponse.fetch(repository.revokeConsent(RevokeConsentRequest(list), authToken))
    }

    fun fetchHipHiuNamesOf(idList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        return repository.getProviderBy(idList)
    }
}

