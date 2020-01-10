package `in`.org.projecteka.jataayu.provider.viewmodel

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
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
    var providersList = emptyList<ProviderInfo>()
    val patientDiscoveryResponse = liveDataOf<PatientDiscoveryResponse>()

    internal var selectedProviderName = String.EMPTY

    fun getProviders(query: String) {
        if (providersList.isEmpty()){
            providerRepository.getProviders(query).enqueue(object : Callback<List<ProviderInfo>?> {
                override fun onFailure(call: Call<List<ProviderInfo>?>, t: Throwable) {
                    Timber.e(t, "Failed to get providers list")
                }
                override fun onResponse(call: Call<List<ProviderInfo>?>, response: Response<List<ProviderInfo>?>) {
                    response.body()?.let {
                        providers.value = response.body()
                        providersList = response.body()!!.toList()
                    }
                 }
            })
        } else{
            providers.postValue(providersList.filter { it.providerIdentifier.name.contains(query, true) })
        }
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

    fun clearList() {
        providersList = emptyList()
    }
}

