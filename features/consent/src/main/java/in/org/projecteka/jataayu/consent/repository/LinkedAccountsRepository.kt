package `in`.org.projecteka.jataayu.consent.repository

import `in`.org.projecteka.jataayu.consent.model.AssociatedAccountsResponse
import `in`.org.projecteka.jataayu.consent.remote.ConsentApis
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import retrofit2.Call

interface LinkedAccountsRepository {
    fun getLinkedAccounts(requestId: String): Call<LinkedAccountsResponse>
}

class LinkedAccountsRepositoryImpl(private val consentApi: ConsentApis) : LinkedAccountsRepository {
    override fun getLinkedAccounts(requestId: String): Call<LinkedAccountsResponse> {
        return consentApi.getLinkedAccounts()
    }
}