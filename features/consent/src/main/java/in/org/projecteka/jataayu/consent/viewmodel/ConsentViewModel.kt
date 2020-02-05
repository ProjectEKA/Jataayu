package `in`.org.projecteka.jataayu.consent.viewmodel

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.core.model.*
import `in`.org.projecteka.jataayu.core.model.approveconsent.CareReference
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifact
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifactRequest
import `in`.org.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ConsentViewModel(private val repository: ConsentRepository) : ViewModel() {
    val consentsListResponse = liveDataOf<ConsentsListResponse>()

    internal var selectedProviderName = String.EMPTY

    var requests = emptyList<Consent>()

    val linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()

    val consentArtifactResponse = liveDataOf<ConsentArtifactResponse>()

    var links = emptyList<Links?>()

    fun getConsents(progressDialogCallback: ProgressDialogCallback) {
        repository.getConsents().enqueue(object : Callback<ConsentsListResponse> {
            override fun onFailure(call: Call<ConsentsListResponse>, t: Throwable) {
                Timber.e(t)
                progressDialogCallback.onFailure(t)
            }

            override fun onResponse(
                call: Call<ConsentsListResponse>,
                response: Response<ConsentsListResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        requests = response.body()?.requests!!
                        consentsListResponse.value = response.body()
                    }
                }
                progressDialogCallback.onSuccess(response)
            }
        })
    }

    fun getLinkedAccounts(progressDialogCallback: ProgressDialogCallback) {

        repository.getLinkedAccounts().enqueue(object : Callback<LinkedAccountsResponse?> {
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

    fun grantConsent(
        requestId: String,
        consentArtifacts: List<ConsentArtifact>,
        progressDialogCallback: ProgressDialogCallback
    ) {
        repository.grantConsent(requestId, ConsentArtifactRequest(consentArtifacts))
            .enqueue(object : Callback<ConsentArtifactResponse> {
                override fun onFailure(call: Call<ConsentArtifactResponse>, t: Throwable) {
                    Timber.d("Unable to grant consent")
                    progressDialogCallback.onFailure(t)
                }

                override fun onResponse(
                    call: Call<ConsentArtifactResponse>,
                    response: Response<ConsentArtifactResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { consentArtifactResponse.value = it }
                    }
                    progressDialogCallback.onSuccess(response)
                }
            })
    }

    fun isRequestAvailable(): Boolean {
        return consentsListResponse.value?.requests!!.isNotEmpty()
    }

    fun populateFilterItems(resources: Resources): List<String> {
        val items = ArrayList<String>(3)
        items.add(String.format(resources.getString(R.string.status_all_requests), requests.size))
        items.add(
            getFormattedItem(
                resources.getString(R.string.status_active_requests),
                RequestStatus.REQUESTED
            )
        )
        items.add(
            getFormattedItem(
                resources.getString(R.string.status_expired_requests),
                RequestStatus.EXPIRED
            )
        )
        return items
    }

    private fun getFormattedItem(filterItem: String, requestStatus: RequestStatus): String {
        var count = 0
        requests.forEach {
            if (requestStatus == it.status) {
                count++
            }
        }
        return String.format(filterItem, count)
    }

    fun getItems(links: List<Links?>): List<IDataBindingModel> {
        val items = arrayListOf<IDataBindingModel>()
        for (link in links) {
            items.add(link?.hip!!)
            for (careContext in link.careContexts) {
                careContext.contextChecked = true
                items.add(careContext)
            }
        }
        return items
    }

    fun getConsentArtifact(
        links: List<Links?>,
        hiTypeObjects: ArrayList<HiType>,
        permission: Permission
    ): List<ConsentArtifact> {
        val consentArtifactList =  ArrayList<ConsentArtifact>()

        val hiTypes = ArrayList<String>()
        hiTypeObjects.forEach { hiTypes.add(it.type) }

        links.forEach {link ->
            val careReferences = ArrayList<CareReference>()
            link!!.careContexts.forEach { careReferences.add(newCareReference(link, it)) }

            consentArtifactList.add(ConsentArtifact(hiTypes, link.hip, careReferences, permission))
        }
        return consentArtifactList
    }

    private fun newCareReference(
        link: Links,
        it: CareContext
    ) = CareReference(link.referenceNumber!!, it.referenceNumber)
}

