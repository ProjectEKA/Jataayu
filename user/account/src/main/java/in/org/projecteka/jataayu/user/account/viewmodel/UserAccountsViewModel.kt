package `in`.org.projecteka.jataayu.user.account.viewmodel

import `in`.org.projecteka.jataayu.core.model.LinkedAccount
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.core.model.LinkedCareContext
import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.network.utils.observeOn
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel

class UserAccountsViewModel(private val repository: UserAccountsRepository) : ViewModel() {
    var linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()

    fun getUserAccounts(responseCallback: ResponseCallback) {
        repository.getUserAccounts().observeOn(linkedAccountsResponse, responseCallback)
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