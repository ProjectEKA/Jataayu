package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.core.model.Hip
import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import com.google.gson.annotations.SerializedName
import retrofit2.Call

interface ProviderRepository {
    fun getProviders(name: String): Call<List<ProviderInfo>>
    fun getPatientAccounts(hip: Hip): Call<PatientDiscoveryResponse>
    fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse>
}

class ProviderRepositoryImpl(private val providerApi: ProviderApis) : ProviderRepository {
    override fun getProviders(name: String): Call<List<ProviderInfo>> {
        return providerApi.getProviders(name)
    }

    override fun getPatientAccounts(hip: Hip): Call<PatientDiscoveryResponse> {
        return providerApi.getPatientAccounts(Hip("10000005"))
    }

    override fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse> {
        return providerApi.linkPatientAccounts(patientDiscoveryResponse)
    }

}

data class Hip(@SerializedName("providerId") val providerId: String)

