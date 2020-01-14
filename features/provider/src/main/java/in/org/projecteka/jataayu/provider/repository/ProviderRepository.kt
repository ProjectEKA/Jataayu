package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import retrofit2.Call

interface ProviderRepository {
    fun getProviders(name: String): Call<List<ProviderInfo>>
    fun getPatientAccounts(identifier : String): Call<PatientDiscoveryResponse>
    fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse>
}

class ProviderRepositoryImpl(private val providerApi: ProviderApis) : ProviderRepository {
    override fun getProviders(name: String): Call<List<ProviderInfo>> {
        return providerApi.getProviders(name)
    }

    override fun getPatientAccounts(identifier: String): Call<PatientDiscoveryResponse> {
        return providerApi.getPatientAccounts(identifier)
    }

    override fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse> {
        return providerApi.linkPatientAccounts(patientDiscoveryResponse)
    }

}