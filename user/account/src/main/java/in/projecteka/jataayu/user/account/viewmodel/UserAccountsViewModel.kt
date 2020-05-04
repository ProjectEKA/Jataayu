package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.presentation.callback.IGroupDataBindingModel
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.extension.liveDataOf
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.regex.Pattern

class UserAccountsViewModel(private val repository: UserAccountsRepository,
                            val preferenceRepository: PreferenceRepository,
                            val credentialRepository: CredentialsRepository) : BaseViewModel() {
    var linkedAccountsResponse = liveDataOf<LinkedAccountsResponse>()
    var createAccountResponse = liveDataOf<CreateAccountResponse>()
    var myProfileResponse = liveDataOf<MyProfile>()
    val logoutResponse = PayloadLiveData<Void>()

    val patientId = ObservableField<String>()
    val patientName = ObservableField<String>()
    val linksSize = ObservableInt()

    val userProfileResponse = MediatorLiveData<PayloadResource<*>>()

    val updateLinks = SingleLiveEvent<List<IGroupDataBindingModel>>()
    val updateProfile = SingleLiveEvent<MyProfile>()
    val addProviderEvent = SingleLiveEvent<Void>()

    val userAccountLoading = ObservableBoolean()
    val myProfileLoading = ObservableBoolean()

    fun fetchAll() {
        userProfileResponse.addSource(getUserAccounts(), Observer {
            when (it) {
                is Loading -> {
                    userAccountLoading.set(it.isLoading)
                    isCurrentlyFetching()
                }
                is Success -> updatePatient(it.data?.linkedPatient)
            }
        })
        userProfileResponse.addSource(getMyProfile(), Observer {
            when (it) {
                is Loading -> {
                    myProfileLoading.set(it.isLoading)
                    isCurrentlyFetching()
                }
                is Success -> {
                    patientName.set(it.data?.name)
                    updateProfile.value = it.data
                }
            }
        })
    }

    private fun updatePatient(linkedPatient: LinkedPatient?) {
        linkedPatient?.run {
            patientId.set(id)
            appBarTitle.set(id)
            updateDisplayAccounts(links)
        }
    }

    fun getUserAccounts() = repository.getUserAccounts()

    fun getMyProfile() = repository.getMyProfile()


    fun updateDisplayAccounts(links: List<Links>?) {
        updateLinks.value = links?.map { link ->
            LinkedAccount(
                link.hip.name,
                link.referenceNumber,
                link.display,
                link.careContexts.map { careContext ->
                    LinkedCareContext(careContext.referenceNumber, careContext.display)
                },
                R.id.childItemsList,
                false
            )
        }.also {
            linksSize.set(it?.size ?: 0)
        }
    }

    private fun isCurrentlyFetching() {
        showProgress(userAccountLoading.get() && myProfileLoading.get())
    }
    fun logout() {
        credentialRepository.refreshToken?.let {
            logoutResponse.fetch(repository.logout(it))
        } ?: kotlin.run {
            logoutResponse.partialFailure(null)
        }
    }

    fun clearSharedPreferences() {
        preferenceRepository.resetPreferences()
        credentialRepository.reset()
    }

    fun isValid(text: String, criteria: String): Boolean {
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun onClickAddProvider() {
        addProviderEvent.call()
    }

}