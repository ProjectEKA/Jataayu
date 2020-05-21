package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository

class AccountCreationActivityViewModel(val repository: UserAccountsRepository,
                                       val preferenceRepository: PreferenceRepository,
                                       val credentialsRepository: CredentialsRepository) : BaseViewModel(){

    companion object {
    }

    var ayushmanId = ""
    var fullName = ""
    var yearOfBirth: Int? = null
    var gender = ""
    val currentPage = SingleLiveEvent<ShowPage>();
    val createAccountResponse = PayloadLiveData<CreateAccountResponse>()

    enum class ShowPage{
        FIRST_SCREEN,
        SECOND_SCREEN
    }

    fun redirectToConfirmAccountPage(fullName: String, ayushmanId: String, yearOfBirth: Int?, gender: String){
        this.ayushmanId = ayushmanId
        this.fullName = fullName
        this.yearOfBirth = yearOfBirth
        this.gender = gender
        currentPage.value = ShowPage.SECOND_SCREEN
    }

    fun redirectToCreateAccountPage(){
        currentPage.value = ShowPage.FIRST_SCREEN
    }

    fun getAuthTokenWithTokenType(response: CreateAccountResponse?): String {
        return "${response?.tokenType?.capitalize()} ${response?.accessToken}"
    }
}