package `in`.org.projecteka.jataayu.consent.viewmodel

import `in`.org.projecteka.jataayu.consent.repository.LinkedAccountsRepository
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.core.model.Links
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ConfirmConsentViewModel(val repository: LinkedAccountsRepository) : ViewModel() {

    val linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()
    var links = emptyList<Links?>()

    fun getLinkedAccounts(requestId: String, progressDialogCallback: ProgressDialogCallback) {
        repository.getLinkedAccounts(requestId).enqueue(object : Callback<LinkedAccountsResponse?> {
            override fun onFailure(call: Call<LinkedAccountsResponse?>, t: Throwable) {
                Timber.d("Unable to fetch linked accounts")
                progressDialogCallback.onFailure(t)
            }

            override fun onResponse(
                call: Call<LinkedAccountsResponse?>,
                response: Response<LinkedAccountsResponse?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.linkedPatient?.links?.let {
                        links = it
                        linkedAccountsResponse.value = response.body()
                    }
                }
                progressDialogCallback.onSuccess(response)
            }
        })
    }
}