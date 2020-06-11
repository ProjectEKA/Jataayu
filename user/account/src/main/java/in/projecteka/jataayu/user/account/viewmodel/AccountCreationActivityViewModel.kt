package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
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
    var cmId = ""
    private set
    var password: String? = null
    private set
    val currentPage = SingleLiveEvent<ShowPage>()


    enum class ShowPage{
        ACCOUNT_INFO_SCREEN,
        CONFIRM_ACCOUNT_SCREEN,
        SUCCESS_SCREEN
    }

    fun redirectToConfirmAccountPage(fullName: String, ayushmanId: String, yearOfBirth: Int?, gender: String){
        this.ayushmanId = ayushmanId
        this.fullName = fullName
        this.yearOfBirth = yearOfBirth
        this.gender = gender
        currentPage.value = ShowPage.CONFIRM_ACCOUNT_SCREEN
    }

    fun redirectToCreateAccountPage(){
        currentPage.value = ShowPage.ACCOUNT_INFO_SCREEN
    }
    fun redirectToCreateAccountSuccessPage(cmId: String, password: String){
        this.password = password
        this.cmId = cmId
        currentPage.value = ShowPage.SUCCESS_SCREEN
    }
}