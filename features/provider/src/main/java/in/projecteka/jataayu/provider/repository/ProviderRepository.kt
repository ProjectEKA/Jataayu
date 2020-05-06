package `in`.projecteka.jataayu.provider.repository

import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.provider.model.*
import `in`.projecteka.jataayu.provider.remote.ProviderApis
import retrofit2.Call

interface ProviderRepository {
    fun getProviders(name: String): Call<List<ProviderInfo>>
    fun getPatientAccounts(request: Request): Call<PatientDiscoveryResponse>
    fun linkPatientAccounts(linkPatientAccountRequest: LinkPatientAccountRequest): Call<LinkAccountsResponse>
    fun verifyOtp(referenceNumber: String, otp: Otp): Call<SuccessfulLinkingResponse>
    fun getProviderBy(providerId: String): Call<ProviderInfo>
}

 class ProviderRepositoryImpl(private val providerApi: ProviderApis) : ProviderRepository {
    override fun getProviders(name: String): Call<List<ProviderInfo>> {
        return providerApi.getProviders(name)
    }

    override fun getPatientAccounts(request: Request): Call<PatientDiscoveryResponse> {
        return providerApi.getPatientAccounts(request)
    }

    override fun linkPatientAccounts(linkPatientAccountRequest: LinkPatientAccountRequest): Call<LinkAccountsResponse> {
        return providerApi.linkPatientAccounts(linkPatientAccountRequest)
    }

    override fun verifyOtp(referenceNumber: String, otp: Otp): Call<SuccessfulLinkingResponse> {
        return providerApi.verifyOtp(referenceNumber, otp)
    }

    override fun getProviderBy(providerId: String): Call<ProviderInfo> {
        return providerApi.getProvidersBy(providerId)
    }
}