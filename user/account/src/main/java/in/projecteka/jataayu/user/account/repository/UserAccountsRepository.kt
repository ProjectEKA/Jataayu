package `in`.projecteka.jataayu.user.account.repository

import `in`.projecteka.jataayu.core.model.CreateAccountRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.MyProfile
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.user.account.remote.UserAccountApis
import retrofit2.Call

interface UserAccountsRepository {
    fun getUserAccounts(): PayloadLiveData<LinkedAccountsResponse>
    fun createAccount(createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse>
    fun getMyProfile(): PayloadLiveData<MyProfile>
    fun logout(refreshToken: String): Call<Void>
}

class UserAccountsRepositoryImpl(private val userAccountApis: UserAccountApis) : UserAccountsRepository {
    override fun getUserAccounts(): PayloadLiveData<LinkedAccountsResponse> {
        val liveData = PayloadLiveData<LinkedAccountsResponse>()
        liveData.fetch(userAccountApis.getUserAccounts())
        return liveData
    }

    override fun createAccount(createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse> {
        return userAccountApis.createAccount(createAccountRequest)
    }

    override fun getMyProfile(): PayloadLiveData<MyProfile> {
        val liveData = PayloadLiveData<MyProfile>()
        liveData.fetch(userAccountApis.getMyProfile())
        return liveData
    }

    override fun logout(refreshToken: String): Call<Void> {
        return userAccountApis.logout(mapOf("refreshToken" to refreshToken))
    }
}
