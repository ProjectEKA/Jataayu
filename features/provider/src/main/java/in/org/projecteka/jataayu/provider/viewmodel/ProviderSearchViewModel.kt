package `in`.org.projecteka.jataayu.provider.viewmodel

import `in`.org.projecteka.jataayu.provider.model.Patient
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProviderSearchViewModel(val providerRepository : ProviderRepository) : ViewModel() {
    val providers = liveDataOf<List<ProviderInfo>>()

    val patients = liveDataOf<List<Patient>>()

    internal val mobile = "9876543210"

    internal var selectedProviderName = String.EMPTY

    fun getProviders(query : String) {
        providerRepository.getProviders(query).enqueue(object : Callback<List<ProviderInfo>?> {
            override fun onFailure(call : Call<List<ProviderInfo>?>, t : Throwable) {
                Timber.e(t, "Failed to get providers list")
            }

            override fun onResponse(call : Call<List<ProviderInfo>?>,
                                    response : Response<List<ProviderInfo>?>) {
                response.body()?.let { providers.value = response.body() }
            }
        })
    }

    fun getPatients(identifier : String) {
        providerRepository.getPatients(identifier).enqueue(object : Callback<List<Patient>?> {
            override fun onFailure(call : Call<List<Patient>?>, t : Throwable) {
                Timber.e(t, "Failed to get patients list")
            }

            override fun onResponse(call : Call<List<Patient>?>,
                                    response : Response<List<Patient>?>) {
                response.body()?.let { patients.value = response.body() }
            }
        })
    }
}

