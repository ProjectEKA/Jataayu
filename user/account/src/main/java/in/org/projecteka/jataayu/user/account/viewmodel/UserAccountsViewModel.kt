package `in`.org.projecteka.jataayu.user.account.viewmodel

import `in`.org.projecteka.jataayu.core.model.LinkedAccount
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.core.model.LinkedCareContext
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class UserAccountsViewModel(private val repository: UserAccountsRepository) : ViewModel() {
    var linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()

    fun getUserAccounts() {
        repository.getUserAccounts().enqueue(object : Callback<LinkedAccountsResponse> {
            override fun onFailure(call: Call<LinkedAccountsResponse>, t: Throwable) {
                Timber.e(t)
            }

            override fun onResponse(call: Call<LinkedAccountsResponse>, response: Response<LinkedAccountsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        linkedAccountsResponse.value = it
                    }
                }
            }
        })
    }

    fun getDisplayAccounts(): List<IDataBindingModel> {
        val items = arrayListOf<IDataBindingModel>()
        linkedAccountsResponse.value?.linkedPatient?.links?.let {
            val links = linkedAccountsResponse.value?.linkedPatient?.links!!
            for (link in links) {
                items.add(LinkedAccount(link?.hip!!.name, link.referenceNumber, link.display))
                for (careContext in link.careContexts) {
                    items.add(LinkedCareContext(careContext.referenceNumber, careContext.display))
                }
            }
        }
        return items
    }
}