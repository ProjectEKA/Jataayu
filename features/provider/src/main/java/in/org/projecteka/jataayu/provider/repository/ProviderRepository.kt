package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.provider.model.Patient
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import retrofit2.Call

interface ProviderRepository {
    fun getProviders(name: String): Call<List<ProviderInfo>>
    fun getPatients(identifier : String): Call<List<Patient>>
}

class ProviderRepositoryImpl(private val providerApi: ProviderApis) : ProviderRepository {
    override fun getProviders(name: String): Call<List<ProviderInfo>> {
        return providerApi.getProviders(name)
    }

    override fun getPatients(identifier : String) : Call<List<Patient>> {
        return providerApi.getPatients(identifier)
    }
}