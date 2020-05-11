package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.callback.PaginationEventCallback
import `in`.projecteka.jataayu.consent.listners.PaginationScrollListener
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.HipHiuNameResponse
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class GrantedConsentListViewModel(private val repository: ConsentRepository,
                                  private val credentialsRepository: CredentialsRepository) : BaseViewModel(), PaginationEventCallback {

    companion object {
        private const val VISIBLE_THRESHOLD = 2
        private const val LIMIT = 10
        private var FILTERS = "status==${RequestStatus.GRANTED.name}"

    }

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()
    val grantedConsentDetailsResponse = PayloadLiveData<List<GrantedConsentDetailsResponse>>()
    val revokeConsentResponse = PayloadLiveData<Void>()
    val grantedConsentsList = MutableLiveData<ConsentsListResponse>()
    val currentStatus = MutableLiveData<RequestStatus>(RequestStatus.GRANTED)
    var scrollListener: PaginationScrollListener? = null


    private val grantedConsentStatusList = listOf(
        R.string.status_all_granted_consents,
        R.string.status_active_granted_consents,
        R.string.status_expired_granted_consents
    )

    internal var selectedProviderName = String.EMPTY

    fun getConsents(limit: Int = LIMIT, offset: Int) {
        consentListResponse.fetch(repository.getConsents(limit, offset, FILTERS))
    }

    fun getGrantedConsentDetails(requestId: String) {
        grantedConsentDetailsResponse.fetch(repository.getGrantedConsentDetails(requestId))
    }


    fun populateFilterItems(resources: Resources, flow: ConsentFlow?): List<String> =
        grantedConsentStatusList.map { getFormattedItem(resources,it, RequestStatus.GRANTED) }


    private fun getFormattedItem(
        resources: Resources,
        filterItem: Int,
        requestStatus: RequestStatus
    ): String {
        val list = grantedConsentsList.value?.requests

        val count = list?.count { consent ->
            val dataExpired = DateTimeUtils.isDateExpired(consent.permission.dataEraseAt)
            when (filterItem) {
                R.string.status_active_granted_consents -> {
                    !dataExpired
                }
                R.string.status_expired_granted_consents -> {
                    dataExpired
                }
                else -> true
            }
        }

        return String.format(resources.getString(filterItem), count)
    }

//    fun filterConsents(consentList: List<Consent>?) {
//        grantedConsentsList.value = consentList?.grantedConsentList()
//    }

    fun revokeConsent(consentArtifactId: String) {
        val list: ArrayList<String> = ArrayList()
        list.add(consentArtifactId)
        revokeConsentResponse.fetch(repository.revokeConsent(RevokeConsentRequest(list), credentialsRepository.consentTemporaryToken))
    }

    fun fetchHipHiuNamesOf(idList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        return repository.getProviderBy(idList)
    }

    override fun loadMoreItems(totalFetchedCount: Int) {
        getConsents(offset = totalFetchedCount)
    }


}

