package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.observeOn
import `in`.projecteka.jataayu.presentation.callback.IGroupDataBindingModel
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import `in`.projecteka.jataayu.util.extension.liveDataOf
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

class UserAccountsViewModel(private val repository: UserAccountsRepository) : ViewModel() {
    var linkedAccountsResponse = liveDataOf<LinkedAccountsResponse?>()
    var createAccountResponse = liveDataOf<CreateAccountResponse?>()
    var myProfileResponse = liveDataOf<MyProfile?>()

    fun getUserAccounts(responseCallback: ResponseCallback) {
        repository.getUserAccounts().observeOn(linkedAccountsResponse, responseCallback)
    }

    fun getMyProfile(responseCallback: ResponseCallback){
        repository.getMyProfile().observeOn(myProfileResponse, responseCallback)
    }

    fun getDisplayAccounts(): List<IGroupDataBindingModel> {
        val items = arrayListOf<IGroupDataBindingModel>()
        linkedAccountsResponse.value?.linkedPatient?.links?.let {
            val links = linkedAccountsResponse.value?.linkedPatient?.links!!
            for (link in links) {
                val careContextsList = arrayListOf<LinkedCareContext>()
                link?.careContexts?.forEach {
                    careContextsList.add(
                        LinkedCareContext(
                            it.referenceNumber,
                            it.display
                        )
                    )
                }
                items.add(
                    LinkedAccount(
                        link?.hip!!.name,
                        link.referenceNumber,
                        link.display,
                        careContextsList,
                        R.id.childItemsList,
                        false
                    )
                )
            }
        }
        return items
    }

    fun createAccount(
        responseCallback: ResponseCallback,
        createAccountRequest: CreateAccountRequest
    ) {
        repository.createAccount(createAccountRequest)
            .observeOn(createAccountResponse, responseCallback)
    }

    fun isValid(text: String, criteria: String): Boolean {
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun getAuthTokenWithTokenType(response: CreateAccountResponse): String {
        return (response.tokenType).capitalize() + CreateAccountFragment.SPACE + response.accessToken
    }
}