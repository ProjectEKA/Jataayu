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
import `in`.projecteka.jataayu.network.utils.isLoading
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import android.content.res.Resources
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class RequestedListViewModel(private val repository: ConsentRepository) : BaseViewModel(), PaginationEventCallback {

    companion object {
        private const val VISIBLE_THRESHOLD = 0
        private const val DEFAULT_LIMIT = 10
        private const val INDEX_ACTIVE = 0
        private const val INDEX_EXPIRED = 1
        private const val INDEX_DENIED = 2
        private const val INDEX_ALL = 3
    }

    val consentListResponse = PayloadLiveData<ConsentsListResponse>()

    private val _requestedConsentsList = MutableLiveData<ConsentsListResponse>()
    val requestedConsentsList: LiveData<ConsentsListResponse>
    get() = _requestedConsentsList

    val paginationScrollListener: PaginationScrollListener = PaginationScrollListener(this)

    private val _currentStatus =  MutableLiveData<RequestStatus>()
    val currentStatus: LiveData<RequestStatus>
    get() = _currentStatus
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
        consentListResponse.fetch(repository.getConsents(limit, offset, currentStatus.value?.name))
    }

    fun populateFilterItems(resources: Resources): List<String>  {
        return requestedConsentStatusList.map { resources.getString(it) }
    }

    override fun loadMoreItems(totalFetchedCount: Int) {
        if (consentListResponse.isLoading()) return
        isLoadingMore.set(View.VISIBLE)
        getConsents(offset = totalFetchedCount)
    }


    fun fetchHipHiuNamesOf(idList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        return repository.getProviderBy(idList)
    }

    fun updateRequestedConsentList(consentsListResponse: ConsentsListResponse) {
        paginationScrollListener.updateTotalSize(consentsListResponse.totalCount)
        _requestedConsentsList.value = consentsListResponse
    }

    fun updateFilterSelectedItem(position: Int) {
        when (position) {
            INDEX_ACTIVE -> _currentStatus.value = RequestStatus.REQUESTED
            INDEX_EXPIRED -> _currentStatus.value = RequestStatus.EXPIRED
            INDEX_DENIED -> _currentStatus.value = RequestStatus.DENIED
            INDEX_ALL -> _currentStatus.value = RequestStatus.ALL
        }
    }
}

