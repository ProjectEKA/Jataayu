package `in`.projecteka.resetpassword.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent

class ResetPasswordActivityViewModel: BaseViewModel() {

    enum class Show{
        FIRST_SCREEN,
        SECOND_SCREEN,
        THIRD_SECREEN
    }

    val addOtpFragmentEvent = SingleLiveEvent<Void>()
    val addResetPasswordFragmentEvent = SingleLiveEvent<Void>()
    var consentManagerId: String? = null
    var sessionId: String? = null
    var tempToken: String? = null

    val redirectTo: SingleLiveEvent<Show> = SingleLiveEvent()

    fun init(){
        redirectTo.value = Show.FIRST_SCREEN
    }

    fun onOtpFragmentRedirectRequest() {
        redirectTo.value = Show.SECOND_SCREEN
    }

    fun onVerifyOtpRedirectRequest() {
        redirectTo.value = Show.THIRD_SECREEN
    }

}