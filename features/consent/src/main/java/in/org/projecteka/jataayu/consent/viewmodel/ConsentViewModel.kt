package `in`.org.projecteka.jataayu.consent.viewmodel

import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsentViewModel(val repository: ConsentRepository) : ViewModel() {
    val consentsListResponse = liveDataOf<ConsentsListResponse>()

    internal var selectedProviderName = String.EMPTY

    fun getConsents() {
        repository.getConsents()
            .enqueue(object : Callback<ConsentsListResponse> {
                override fun onFailure(call: Call<ConsentsListResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ConsentsListResponse>,
                    response: Response<ConsentsListResponse>
                ) {
                    response.body()?.let { consentsListResponse.value = response.body() }
                }
            })
    }
}

