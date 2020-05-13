package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.callback.PaginationEventCallback
import `in`.projecteka.jataayu.consent.listners.PaginationScrollListener
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.HipHiuNameResponse
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import android.content.res.Resources
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class RequestedListViewModel(private val repository: ConsentRepository) : BaseViewModel(), PaginationEventCallback {

    companion object {
        private const val VISIBLE_THRESHOLD = 0
        private const val DEFAULT_LIMIT = 10
    }

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()
    val requestedConsentsList = MutableLiveData<ConsentsListResponse>()
    var scrollListener: PaginationScrollListener? = null

    val currentStatus = MutableLiveData<RequestStatus>(RequestStatus.REQUESTED)
    val isLoadingMore = ObservableInt(View.INVISIBLE)



    private var fetchLimit = DEFAULT_LIMIT
    private var totalFetchedCount = 0


    private val requestedConsentStatusList = listOf(
        R.string.status_active_requested_consents,
        R.string.status_expired_requested_consents,
        R.string.status_denied_consent_requests,
        R.string.status_all_requested_consents
    )


    internal var selectedProviderName = String.EMPTY

    fun getConsents(limit: Int = fetchLimit,
                    offset: Int) {
        fetchLimit = limit
//        val filters = status?.let { "status==${status.name}" }
        consentListResponse.fetch(repository.getConsents(limit, offset, currentStatus.value?.name))
    }

    fun populateFilterItems(resources: Resources): List<String>  {
//        requestedConsentStatusList.map { getFormattedItem(resources,it, REQUESTED)  }
        return requestedConsentStatusList.map { resources.getString(it) }
    }

    override fun loadMoreItems(totalFetchedCount: Int) {
        isLoadingMore.set(View.VISIBLE)
        getConsents(offset = totalFetchedCount)
    }



//    private fun getFormattedItem(
//        resources: Resources,
//        filterItem: Int,
//        requestStatus: RequestStatus
//    ): String {
//        val list = requestedConsentsList.value
//
//        val count = list?.count { consent ->
//            val dataExpired = DateTimeUtils.isDateExpired(consent.permission.dataEraseAt)
//            when (filterItem) {
//                R.string.status_denied_consent_requests -> {
//                    consent.status == DENIED
//                }
//                R.string.status_active_requested_consents -> {
//                    if (consent.status != DENIED) !dataExpired else false
//                }
//                R.string.status_expired_requested_consents -> {
//                    if (consent.status != DENIED) dataExpired else false
//                }
//                else -> true
//            }
//        }
//
//        return String.format(resources.getString(filterItem), count)
//    }

//    fun filterConsents(consentList: List<Consent>?) {
//        requestedConsentsList.value = consentList
//    }

    fun fetchHipHiuNamesOf(idList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        return repository.getProviderBy(idList)
    }
}

