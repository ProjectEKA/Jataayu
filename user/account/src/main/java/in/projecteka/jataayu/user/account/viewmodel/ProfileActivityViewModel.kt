package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import androidx.databinding.ObservableField

class ProfileActivityViewModel: BaseViewModel() {

    var title = ObservableField<String>()

    enum class Show{
        VIEW_PROFILE
    }

    var redirectTo = SingleLiveEvent<Show>()

    fun init(title: String){
        this.title.set(title)
        redirectTo.value = Show.VIEW_PROFILE
    }

}