package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.presentation.callback.IGroupDataBindingModel
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
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
    var linkedAccountsResponse = liveDataOf<List<Links>>()
    var createAccountResponse = liveDataOf<CreateAccountResponse>()
    var myProfileResponse = liveDataOf<MyProfile>()

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
        val accountLiveData = getUserAccounts()
        userProfileResponse.addSource(accountLiveData, Observer {
            when (it) {
                is Loading -> {
                    userAccountLoading.set(it.isLoading)
                    isCurrentlyFetching()
                }
                is Success -> {
                    val linkedPatient = it.data?.linkedPatient
                    linkedAccountsResponse.value = linkedPatient?.links
                    updatePatient(linkedPatient)
                }
            }
        })
        val profileLiveData = getMyProfile()
        userProfileResponse.addSource(profileLiveData, Observer {
            when (it) {
                is Loading -> {
                    myProfileLoading.set(it.isLoading)
                    isCurrentlyFetching()
                }
                is Success -> {
                    saveProfileDetails(it.data)
                    patientName.set(it.data?.name)
                    updateProfile.value = it.data
                }
                is PartialFailure -> {
                }
            }
        })
    }

    private fun saveProfileDetails(profile: MyProfile?) {
        profile?.let {
            preferenceRepository.name = it.name
            preferenceRepository.pinCreated = it.hasTransactionPin
            preferenceRepository.gender = it.gender
            preferenceRepository.consentManagerId = it.id

            it.yearOfBirth?.let {yob ->
                preferenceRepository.yearOfBirth = yob
            }

            it.verifiedIdentifiers?.forEach { identifier ->
                if (identifier.type == PreferenceRepository.VERIFIED_IDENTIFIER_TYPE_MOBILE) {
                    preferenceRepository.countryCode =
                        (identifier.value).substringBeforeLast(PreferenceRepository.MOBILE_NUMBER_DELIMITER) + PreferenceRepository.MOBILE_NUMBER_DELIMITER
                    preferenceRepository.mobileIdentifier =
                        (identifier.value).substringAfter(PreferenceRepository.MOBILE_NUMBER_DELIMITER)
                }
            }


            it.unverifiedIdentifiers?.forEach { unverifiedIdentifier ->
                if (unverifiedIdentifier.type == PreferenceRepository.TYPE_AYUSHMAN_BHARAT_ID){
                    preferenceRepository.ayushmanBharatId = unverifiedIdentifier.value
                }
                if (unverifiedIdentifier.type == PreferenceRepository.TYPE_PAN) {
                    preferenceRepository.pan = unverifiedIdentifier.value
                }
            }
        }
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

    fun getHipHiuNamesByIdList(idList: List<HipHiuIdentifiable>) = repository.getProviderBy(idList)


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

    fun isValid(text: String, criteria: String): Boolean {
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun onClickAddProvider() {
        addProviderEvent.call()
    }

}