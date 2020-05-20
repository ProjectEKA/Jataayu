package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository

class RedirectingActivity (private val repository: UserAccountsRepository,
                           val preferenceRepository: PreferenceRepository,
                           val credentialsRepository: CredentialsRepository) : BaseViewModel(){

    enum class ShowPage{
        FIRST_SCREEN,
        SECOND_SCREEN
    }
}
