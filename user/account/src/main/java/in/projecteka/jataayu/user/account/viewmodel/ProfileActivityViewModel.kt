package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent

class ProfileActivityViewModel: BaseViewModel() {

    enum class Show{
        VIEW_PROFILE
    }

    var redirectTo = SingleLiveEvent<Show>()

    fun init(){
        redirectTo.value = Show.VIEW_PROFILE
    }

}