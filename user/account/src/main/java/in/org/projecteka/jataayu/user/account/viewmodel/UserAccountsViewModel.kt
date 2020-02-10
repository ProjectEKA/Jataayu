package `in`.org.projecteka.jataayu.user.account.viewmodel

import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.core.model.LinkedAccount
import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.core.model.LinkedCareContext
import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.network.utils.observeOn
import `in`.org.projecteka.jataayu.presentation.callback.IGroupDataBindingModel
import `in`.org.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.org.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel

class UserAccountsViewModel(private val repository: UserAccountsRepository) : ViewModel() {
    var linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()

    fun getUserAccounts(responseCallback: ResponseCallback) {
        repository.getUserAccounts().observeOn(linkedAccountsResponse, responseCallback)
    }

    fun getDisplayAccounts(): List<IGroupDataBindingModel> {
        val items = arrayListOf<IGroupDataBindingModel>()
        linkedAccountsResponse.value?.linkedPatient?.links?.let {
            val links = linkedAccountsResponse.value?.linkedPatient?.links!!
            for (link in links) {
                val careContextsList =  arrayListOf<LinkedCareContext>()
                link?.careContexts?.forEach { careContextsList.add(LinkedCareContext(it.referenceNumber, it.display)) }
                items.add(LinkedAccount(link?.hip!!.name, link.referenceNumber, link.display, careContextsList, R.id.childItemsList,false))
            }
        }
        return items
    }
}