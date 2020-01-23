package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.core.model.Request
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.org.projecteka.jataayu.provider.model.Token
import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import retrofit2.Call

interface ProviderRepository {
    fun getProviders(name: String): Call<List<ProviderInfo>>
    fun getPatientAccounts(request: Request): Call<PatientDiscoveryResponse>
    fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse>
    fun verifyOtp(referenceNumber: String, token: Token): Call<SuccessfulLinkingResponse>
}

class ProviderRepositoryImpl(private val providerApi: ProviderApis) : ProviderRepository {
    override fun getProviders(name: String): Call<List<ProviderInfo>> {
        return providerApi.getProviders(name)
    }

    override fun getPatientAccounts(request: Request): Call<PatientDiscoveryResponse> {
        return providerApi.getPatientAccounts(request)
    }

    override fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse): Call<LinkAccountsResponse> {
        return providerApi.linkPatientAccounts(patientDiscoveryResponse)
    }

    override fun verifyOtp(referenceNumber: String, token: Token): Call<SuccessfulLinkingResponse> {
        return providerApi.verifyOtp(referenceNumber, token)
    }
}