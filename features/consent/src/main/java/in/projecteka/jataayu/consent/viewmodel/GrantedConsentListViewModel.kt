package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.callback.PaginationEventCallback
import `in`.projecteka.jataayu.consent.listners.PaginationScrollListener
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.RequestStatus.*
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.isLoading
import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import android.content.res.Resources
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class GrantedConsentListViewModel(private val repository: ConsentRepository,
                                  private val credentialsRepository: CredentialsRepository) : BaseViewModel(), PaginationEventCallback {

    companion object {
        const val VISIBLE_THRESHOLD = 2
        const val LIMIT = 10
        const val INDEX_GRANTED = 0
        const val INDEX_REVOKED = 1
        const val INDEX_EXPIRED = 2
        const val INDEX_ALL = 3

    }

    val consentArtifactResponse = PayloadLiveData<ConsentArtifactResponse>()
    val grantedConsentDetailsResponse = PayloadLiveData<List<GrantedConsentDetailsResponse>>()
    val revokeConsentResponse = PayloadLiveData<Void>()
    val isLoadingMore = ObservableInt(View.INVISIBLE)

    private val _consentArtifactList = MutableLiveData<ConsentArtifactResponse>()
    val consentArtifactList: LiveData<ConsentArtifactResponse>
    get() = _consentArtifactList



    private val _currentStatus = MutableLiveData<RequestStatus>(GRANTED)
    val currentStatus : LiveData<RequestStatus>
    get() = _currentStatus



    val paginationScrollListener: PaginationScrollListener = PaginationScrollListener(this)


    private val grantedConsentStatusList = listOf(
        R.string.status_active_granted_consents,
        R.string.status_revoked_consents,
        R.string.status_expired_granted_consents,
        R.string.status_all_consents_artifacts
    )

    internal var selectedProviderName = String.EMPTY

    fun getConsents(limit: Int = LIMIT, offset: Int) {
        consentArtifactResponse.fetch(repository.getConsentsArtifactList(limit, offset, currentStatus.value!!))
    }

    fun getGrantedConsentDetails(requestId: String) {
        grantedConsentDetailsResponse.fetch(repository.getGrantedConsentDetails(requestId))
    }

    fun populateFilterItems(resources: Resources): List<String> =
        grantedConsentStatusList.map { resources.getString(it) }


    fun revokeConsent(consentArtifactId: String) {
        val list: ArrayList<String> = ArrayList()
        list.add(consentArtifactId)
        revokeConsentResponse.fetch(repository.revokeConsent(RevokeConsentRequest(list), credentialsRepository.consentTemporaryToken))
    }

    fun fetchHipHiuNamesOf(idList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        return repository.getProviderBy(idList)
    }

    override fun loadMoreItems(totalFetchedCount: Int) {
        if (consentArtifactResponse.isLoading()) return
        isLoadingMore.set(View.VISIBLE)
        getConsents(offset = totalFetchedCount)
    }

    fun updateFilterSelectedItem(position: Int) {
        when (position) {
            INDEX_GRANTED -> _currentStatus.postValue(GRANTED)
            INDEX_EXPIRED -> _currentStatus.postValue(EXPIRED)
            INDEX_REVOKED -> _currentStatus.postValue(REVOKED)
            INDEX_ALL -> _currentStatus.postValue(ALL)
        }
    }

    fun updateConsentArtifactList(response: ConsentArtifactResponse) {
        paginationScrollListener.updateTotalSize(response.size)
        _consentArtifactList.value = response
    }

    fun getNoConsentMessage(): Int {
        return Consent.getNoConsentMessageResourceId(currentStatus.value!!, ConsentType.ARTIFACTS)
    }

}

