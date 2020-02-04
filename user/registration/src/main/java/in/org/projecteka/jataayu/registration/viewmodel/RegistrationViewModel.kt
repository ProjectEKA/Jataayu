package `in`.org.projecteka.jataayu.registration.viewmodel

import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.org.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.org.projecteka.jataayu.registration.repository.AuthorizationRepository
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RegistrationViewModel(val repository: AuthorizationRepository) : ViewModel() {
    var requestVerificationResponse = liveDataOf<RequestVerificationResponse>()
    var verifyIdentifierResponse = liveDataOf<VerifyIdentifierResponse>()

    fun requestVerification(
        identifierType: String, identifier: String,
        progressDialogCallback: ProgressDialogCallback
    ) {

        repository.requestVerification(RequestVerificationRequest(identifierType, identifier))
            .enqueue(object : Callback<RequestVerificationResponse> {
                override fun onFailure(call: Call<RequestVerificationResponse>, t: Throwable) {
                    Timber.e(t)
                    progressDialogCallback.onFailure(t)
                }

                override fun onResponse(call: Call<RequestVerificationResponse>, response: Response<RequestVerificationResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            requestVerificationResponse.value = response.body()
                        }
                    }
                    progressDialogCallback.onSuccess(response)
                }
            })
    }

    fun verifyIdentifier(requestVerificationResponse: RequestVerificationResponse,
                         progressDialogCallback: ProgressDialogCallback) {
        repository.verifyIdentifier(requestVerificationResponse).enqueue(object: Callback<VerifyIdentifierResponse>{
            override fun onFailure(call: Call<VerifyIdentifierResponse>, t: Throwable) {
                Timber.e(t)
                progressDialogCallback.onFailure(t)
            }

            override fun onResponse(
                call: Call<VerifyIdentifierResponse>,
                response: Response<VerifyIdentifierResponse>
            ) {
                if (response.isSuccessful) {
                    response.let {
                        verifyIdentifierResponse.value = response.body()
                    }
                    progressDialogCallback.onSuccess(response)
                } else {
                    progressDialogCallback.onFailure(response)
                }
            }
        })
    }
}