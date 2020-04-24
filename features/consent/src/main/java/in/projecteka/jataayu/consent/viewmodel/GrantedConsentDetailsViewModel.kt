package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.Cache.ConsentDataProviderCacheManager
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.LinkedCareContext
import `in`.projecteka.jataayu.core.model.Links
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.LinkedHip
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.util.extension.EMPTY
import androidx.lifecycle.ViewModel

class GrantedConsentDetailsViewModel(private val repository: ConsentRepository) : ViewModel() {

    val linkedAccountsResponse = PayloadLiveData<LinkedAccountsResponse>()
    val grantedConsentDetailsResponse = PayloadLiveData<List<GrantedConsentDetailsResponse>>()
    internal var selectedProviderName = String.EMPTY
    private val consentDataProviderCacheManager = ConsentDataProviderCacheManager()

    fun getLinkedAccounts() =
        linkedAccountsResponse.fetch(repository.getLinkedAccounts())

    fun getConsentRepository(): ConsentRepository = repository

    fun getGrantedConsentDetails(requestId: String) {
        grantedConsentDetailsResponse.fetch(repository.getGrantedConsentDetails(requestId))
    }


    fun getItems(grantedConsents: List<GrantedConsentDetailsResponse>, linkedAccounts: List<Links>?): Pair<List<IDataBindingModel>,Int> {
        var count = 0
        val items = arrayListOf<IDataBindingModel>()
        for (grantedConsent in grantedConsents) {
            val grantedAccountHipId = grantedConsent.consentDetail.hip?.id
            linkedAccounts?.forEach { link ->
                if (grantedAccountHipId == link.hip.id) {
                    // As per the requirement get the HIP name from ID
                    val linkedHipName = consentDataProviderCacheManager.getProviderBy(grantedAccountHipId)?.name ?: ""
                    val linkedHip = LinkedHip(linkedHipName, link.referenceNumber)
                    items.add(linkedHip)
                    count++
                    val careContextsList = arrayListOf<LinkedCareContext>()
                    link.careContexts.forEach { careContextsList.add(LinkedCareContext(it.referenceNumber, it.display)) }
                    (careContextsList).forEach { linkedAccountCareContext ->
                        (grantedConsent.consentDetail.careContexts)?.forEach { grantedAccountCareContext ->
                            grantedAccountCareContext.careContextReference.toString()
                            if (linkedAccountCareContext.referenceNumber == grantedAccountCareContext.careContextReference) {
                                items.add(linkedAccountCareContext)
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
        return Pair(items, count)
    }
}

