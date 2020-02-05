package `in`.org.projecteka.jataayu.provider.viewmodel

import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.core.model.Request
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.org.projecteka.jataayu.provider.model.Token
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
    val linkAccountsResponse = liveDataOf<LinkAccountsResponse>()
    val successfulLinkingResponse = liveDataOf<SuccessfulLinkingResponse>()

    internal var selectedProviderName = String.EMPTY

    fun getProviders(query: String) {
        if (providersList.isEmpty()) {
            providerRepository.getProviders(query).enqueue(object : Callback<List<ProviderInfo>?> {
                override fun onFailure(call: Call<List<ProviderInfo>?>, t: Throwable) {
                    Timber.e(t, "Failed to get providers list")
                }

                override fun onResponse(call: Call<List<ProviderInfo>?>, response: Response<List<ProviderInfo>?>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            providers.value = it
                            providersList = it.toList()
                        }
                    }
                }
            })
        } else {
            providers.postValue(providersList.filter { it.hip.name.contains(query, true) })
        }
    }

    fun getPatientAccounts(request: Request, progressDialogCallback: ProgressDialogCallback) {
        providerRepository.getPatientAccounts(request).enqueue(object : Callback<PatientDiscoveryResponse> {
            override fun onFailure(call: Call<PatientDiscoveryResponse>, t: Throwable) {
                Timber.e(t, "Failed to get patients accounts")
                progressDialogCallback.onFailure(t)
            }

            override fun onResponse(
                call: Call<PatientDiscoveryResponse>,
                response: Response<PatientDiscoveryResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { patientDiscoveryResponse.value = it }
                    progressDialogCallback.onSuccess(response)
                } else {
                    progressDialogCallback.onFailure(response)
                }
            }
        })
    }

    fun linkPatientAccounts(patientDiscoveryResponse: PatientDiscoveryResponse, progressDialogCallback: ProgressDialogCallback) {
        providerRepository.linkPatientAccounts(patientDiscoveryResponse)
            .enqueue(object : Callback<LinkAccountsResponse> {
                override fun onFailure(call: Call<LinkAccountsResponse>, t: Throwable) {
                    Timber.e(t, "Failed to link patients accounts")
                    progressDialogCallback.onFailure(t)
                }

                override fun onResponse(call: Call<LinkAccountsResponse>, response: Response<LinkAccountsResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { linkAccountsResponse.value = it }
                    }
                    progressDialogCallback.onSuccess(response)
                }
            })
    }

    fun verifyOtp(referenceNumber: String, token: Token, progressDialogCallback: ProgressDialogCallback) {
        providerRepository.verifyOtp(referenceNumber, token).enqueue(object: Callback<SuccessfulLinkingResponse> {
            override fun onFailure(call: Call<SuccessfulLinkingResponse>, t: Throwable) {
                Timber.e(t)
                progressDialogCallback.onFailure(t)
            }

            override fun onResponse(call: Call<SuccessfulLinkingResponse>, response: Response<SuccessfulLinkingResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { successfulLinkingResponse.value = it }
                }
                progressDialogCallback.onSuccess(response)
            }
        })
    }

    fun canLinkAccounts(): Boolean {
        for (careContext in patientDiscoveryResponse.value?.patient?.careContexts!!) {
            if (careContext.contextChecked) {
                return true
            }
        }
        return false
    }

    fun clearList() {
        providersList = emptyList()
    }
}

