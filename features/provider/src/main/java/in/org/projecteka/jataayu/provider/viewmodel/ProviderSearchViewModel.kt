package `in`.org.projecteka.jataayu.provider.viewmodel

import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProviderSearchViewModel(val providerRepository: ProviderRepository) : ViewModel() {
    val mobile = "9876543210"
    val providers = liveDataOf<List<ProviderInfo>>()

    val patientDiscoveryResponse = liveDataOf<PatientDiscoveryResponse>()

    internal var selectedProviderName = String.EMPTY

    fun getProviders(query: String) {
        providerRepository.getProviders(query).enqueue(object : Callback<List<ProviderInfo>?> {
            override fun onFailure(call: Call<List<ProviderInfo>?>, t: Throwable) {
                Timber.e(t, "Failed to get providers list")
            }

            override fun onResponse(call: Call<List<ProviderInfo>?>, response: Response<List<ProviderInfo>?>) {
                response.body()?.let { providers.value = response.body() }
            }
        })
    }

    fun getPatientAccounts(identifier: String) {
        providerRepository.getPatientAccounts(identifier)
            .enqueue(object : Callback<PatientDiscoveryResponse> {
                override fun onFailure(call: Call<PatientDiscoveryResponse>, t: Throwable) {
                    Timber.e(t, "Failed to get patients accounts")
                }

                override fun onResponse(call: Call<PatientDiscoveryResponse>, response: Response<PatientDiscoveryResponse>) {
                    response.body()?.let { patientDiscoveryResponse.value = response.body() }
                }

            })
    }

    fun canLinkAccounts() : Boolean{
        for (careContext in patientDiscoveryResponse?.value?.patient?.careContexts!!) {
            if(careContext.contextChecked) {
                return true
            }
        }
        return false
    }
}

