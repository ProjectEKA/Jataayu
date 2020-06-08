package `in`.projecteka.jataayu.core.repository

import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.remote.UserAccountApis
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.network.utils.fetch
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call

interface UserAccountsRepository {
    fun getUserAccounts(): PayloadLiveData<LinkedAccountsResponse>
    fun createAccount(createAccountRequest: CreateAccountRequest): Call<CreateAccountResponse>
    fun getMyProfile(): PayloadLiveData<MyProfile>
    fun logout(refreshToken: String): Call<Void>
    fun getProviderBy(providerIdList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse>
    fun getLoginMode(userName: String): Call<LoginType>
    fun recoverCmid(verifyOTPRequest: VerifyOTPRequest): Call<RecoverCmidResponse>
    fun generateOTPForRecoverCMID(recoverCmidRequest: RecoverCmidRequest): Call<GenerateOTPResponse>
    fun login(username: String, password: String, grantType: String): Call<CreateAccountResponse>
}

class UserAccountsRepositoryImpl(private val userAccountApis: UserAccountApis) :
    UserAccountsRepository {
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

    override fun getProviderBy(providerIdList: List<HipHiuIdentifiable>): MediatorLiveData<HipHiuNameResponse> {
        val idList = providerIdList.toSet().map { it.getId() }
        return getProviderData(idList)
    }

    override fun getLoginMode(userName: String): Call<LoginType> {
        return userAccountApis.getLoginMode(userName)
    }

    override fun recoverCmid(verifyOTPRequest: VerifyOTPRequest): Call<RecoverCmidResponse> {
        return userAccountApis.recoverCmid(verifyOTPRequest)
    }

    override fun generateOTPForRecoverCMID(recoverCmidRequest: RecoverCmidRequest): Call<GenerateOTPResponse> {
        return userAccountApis.generateOTPForRecoverCMID(recoverCmidRequest)
    }

    override fun login(
        username: String,
        password: String,
        grantType: String
    ): Call<CreateAccountResponse> {
       return userAccountApis.login(LoginRequest(username, password, grantType) )
    }

    private var providerLiveDataCount = 0

    private fun getProviderData(idList: List<String>): MediatorLiveData<HipHiuNameResponse> {

        providerLiveDataCount += idList.count()
        val nameResponseMap = HashMap<String, String>()
        val mediatorLiveData = MediatorLiveData<HipHiuNameResponse>()
        idList.forEach { id ->
            val liveData = PayloadLiveData<ProviderInfo>()
            liveData.fetch(userAccountApis.getProvidersBy(id))
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
