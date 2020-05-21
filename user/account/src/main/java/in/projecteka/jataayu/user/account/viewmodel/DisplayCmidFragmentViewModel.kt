package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.text.SpannableStringBuilder
import androidx.databinding.ObservableField

class DisplayCmidFragmentViewModel() : BaseViewModel() {

    enum class Redirect {
        BACK_TO_LOGIN,
        REVIEW
    }

    val cmidTitle = ObservableField<SpannableStringBuilder>()
    val redirectToEvent = SingleLiveEvent<Redirect>()

    fun init(cmidTitle: SpannableStringBuilder) {
        this.cmidTitle.set(cmidTitle)
    }

    fun onBackToLoginClick() {
        redirectToEvent.value = Redirect.BACK_TO_LOGIN
    }

    fun onReview(){
        redirectToEvent.value = Redirect.REVIEW
    }
}