package `in`.org.projecteka.jataayu.consent.repository

import `in`.org.projecteka.jataayu.consent.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.consent.remote.ConsentApis
import retrofit2.Call

interface LinkedAccountsRepository {
    fun getLinkedAccounts(requestId: String): Call<LinkedAccountsResponse>
}

class LinkedAccountsRepositoryImpl(private val consentApi: ConsentApis) : LinkedAccountsRepository {
    override fun getLinkedAccounts(requestId: String): Call<LinkedAccountsResponse> {
        return consentApi.getLinkedAccounts()
    }
}