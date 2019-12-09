package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.remote.ProviderSearchApi
import retrofit2.Call

interface ProviderRepository {
    fun getProvider(name: String): Call<List<ProviderInfo>>
}

class ProviderRepositoryImpl(private val providerSearchApi: ProviderSearchApi) : ProviderRepository {
    override fun getProvider(name: String): Call<List<ProviderInfo>> {
        return providerSearchApi.getProvider(name)
    }
}