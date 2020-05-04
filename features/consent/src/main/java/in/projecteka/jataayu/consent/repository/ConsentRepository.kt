package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.model.RevokeConsentRequest
import `in`.projecteka.jataayu.consent.remote.ConsentApis
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.HipHiuNameResponse
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.network.utils.fetch
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call

interface ConsentRepository {
    fun getConsents(): Call<ConsentsListResponse>
    fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest, authToken: String?): Call<ConsentArtifactResponse>
    fun getLinkedAccounts(): Call<LinkedAccountsResponse>
    fun revokeConsent(revokeConsentRequest: RevokeConsentRequest, authToken: String?): Call<Void>
    fun getGrantedConsentDetails(requestId: String): Call<List<GrantedConsentDetailsResponse>>
    fun denyConsent(requestId: String): Call<Void>
    fun getProviderBy(providerIdList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse>
}

class ConsentRepositoryImpl(private val consentApi: ConsentApis) : ConsentRepository {
    override fun grantConsent(requestId: String, approveConsentRequest: ConsentArtifactRequest, authToken: String?):
            Call<ConsentArtifactResponse> {
        return consentApi.approveConsent(requestId, approveConsentRequest, authToken)
    }

    override fun getConsents(): Call<ConsentsListResponse> {
        return consentApi.getConsents()
    }
    override fun getLinkedAccounts(): Call<LinkedAccountsResponse> {
        return consentApi.getLinkedAccounts()
    }

    override fun revokeConsent(revokeConsentRequest: RevokeConsentRequest, authToken: String?): Call<Void> {
        return consentApi.revokeConsent(revokeConsentRequest, authToken)
    }

    override fun getGrantedConsentDetails(requestId: String): Call<List<GrantedConsentDetailsResponse>> {
        return consentApi.getGrantedConsentDetails(requestId)
    }

    override fun denyConsent(requestId: String): Call<Void> {
        return consentApi.denyConsent(requestId)
    }

    override fun getProviderBy(providerIdList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        val idList = providerIdList.toSet().map { it.getId() }
        return getProviderData(idList)
    }

    private var providerLiveDataCount = 0

    private fun getProviderData(idList: List<String>): MediatorLiveData<HipHiuNameResponse> {

        providerLiveDataCount += idList.count()
        var nameResponseMap = HashMap<String, String>()
        val mediatorLiveData = MediatorLiveData<HipHiuNameResponse>()
        idList.forEach { id ->
            val liveData = PayloadLiveData<ProviderInfo>()
            liveData.fetch(consentApi.getProvidersBy(id))
            mediatorLiveData.addSource(liveData) { response ->
                when(response) {
                    is Success ->  {
                        response.data?.hip?.let {  nameResponseMap[it.getId()] = it.name }
                        mediatorLiveData.removeSource(liveData)
                        providerLiveDataCount -=1
                    }
                    is Loading -> {}
                    else  -> {
                        mediatorLiveData.removeSource(liveData)
                        providerLiveDataCount -=1
                    }
                }
                if (providerLiveDataCount == 0) {
                    val hipHiuNameResponse = HipHiuNameResponse(true, nameResponseMap)
                    mediatorLiveData.postValue(hipHiuNameResponse)
                }
            }
        }
        return mediatorLiveData
    }
}