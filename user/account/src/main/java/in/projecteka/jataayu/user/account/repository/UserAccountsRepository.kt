package `in`.projecteka.jataayu.user.account.repository

import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.user.account.remote.UserAccountApis
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call

interface UserAccountsRepository {
    fun getUserAccounts() : Call<LinkedAccountsResponse>
    fun createAccount(createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse>
    fun getMyProfile(): Call<MyProfile>
    fun getProviderByIDList(providerIdList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse>
}

class UserAccountsRepositoryImpl(private val userAccountApis: UserAccountApis): UserAccountsRepository {
    override fun getUserAccounts(): Call<LinkedAccountsResponse> {
        return userAccountApis.getUserAccounts()
    }

    override fun createAccount(createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse> {
        return userAccountApis.createAccount(createAccountRequest)
    }

    override fun getMyProfile(): Call<MyProfile> {
        return userAccountApis.getMyProfile()
    }

    override fun getProviderByIDList(providerIdList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
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
            liveData.fetch(userAccountApis.getProvidersByID(id))
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
