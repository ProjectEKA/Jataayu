package `in`.org.projecteka.jataayu.consent.viewmodel

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.RequestStatus
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsentViewModel(val repository: ConsentRepository) : ViewModel() {
    val consentsListResponse = liveDataOf<ConsentsListResponse>()

    internal var selectedProviderName = String.EMPTY

    var requests = emptyList<Consent>()

    fun getConsents() {
        repository.getConsents()
            .enqueue(object : Callback<ConsentsListResponse> {
                override fun onFailure(call: Call<ConsentsListResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<ConsentsListResponse>, response: Response<ConsentsListResponse>) {
                    response.body()?.let {
                        requests = response.body()?.requests!!
                        consentsListResponse.value = response.body()
                    }
                }
            })
    }

    fun isRequestAvailable(): Boolean {
        return consentsListResponse.value?.requests!!.isNotEmpty()
    }

    fun populateFilterItems(resources: Resources): List<String> {
        val items = ArrayList<String>(3)
        items.add(String.format(resources.getString(R.string.status_all_requests), requests.size))
        items.add(getFormattedItem(resources.getString(R.string.status_active_requests), RequestStatus.REQUESTED))
        items.add(getFormattedItem(resources.getString(R.string.status_expired_requests), RequestStatus.EXPIRED))
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
}

