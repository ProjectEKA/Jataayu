package `in`.projecteka.jataayu.user.account.repository

import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.user.account.remote.UserAccountApis
import retrofit2.Call

interface UserAccountsRepository {
    fun getUserAccounts() : Call<LinkedAccountsResponse>
}

class UserAccountsRepositoryImpl(private val userAccountApis: UserAccountApis): UserAccountsRepository {
    override fun getUserAccounts(): Call<LinkedAccountsResponse> {
        return userAccountApis.getUserAccounts()
    }
}
