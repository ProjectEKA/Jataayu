package `in`.projecteka.jataayu.user.account.remote

import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserAccountApis {
    @GET("patients/links")
    fun getUserAccounts(): Call<LinkedAccountsResponse>
}
