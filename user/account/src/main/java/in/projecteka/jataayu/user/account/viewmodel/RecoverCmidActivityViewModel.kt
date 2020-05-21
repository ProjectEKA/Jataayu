package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import androidx.databinding.ObservableField

class RecoverCmidActivityViewModel: BaseViewModel() {

    var cmid = ObservableField<String>()

    enum class Show{
        READ_VALUES_SCREEN,
        DISPLAY_CMID_SCREEN,
        NO_OR_MULTIPLE_MATCHING_RECORDS,
    }

    val addOtpFragmentEvent = SingleLiveEvent<Void>()
    val addResetPasswordFragmentEvent = SingleLiveEvent<Void>()
    var consentManagerId: String? = null
    var sessionId: String? = null
    var tempToken: String? = null

    val redirectTo: SingleLiveEvent<Show> = SingleLiveEvent()

    fun init(){
        redirectTo.value = Show.READ_VALUES_SCREEN
    }

    fun onDisplayCmidRequest() {
        redirectTo.value = Show.DISPLAY_CMID_SCREEN
    }

    fun onReviewRequest() {
        redirectTo.value = Show.NO_OR_MULTIPLE_MATCHING_RECORDS
    }

//    fun onOtpFragmentRedirectRequest() {
//        redirectTo.value = Show.SECOND_SCREEN
//    }

//    fun onVerifyOtpRedirectRequest() {
//        redirectTo.value = Show.THIRD_SECREEN
//    }

}