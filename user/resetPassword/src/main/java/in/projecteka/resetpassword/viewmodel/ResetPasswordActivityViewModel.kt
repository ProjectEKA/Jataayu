package `in`.projecteka.resetpassword.viewmodel

import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent

class ResetPasswordActivityViewModel: BaseViewModel() {

    enum class Show{
        OTP_VERIFICATION_SCREEN,
        SET_PASSWORD_SCREEN
    }

    val addOtpFragmentEvent = SingleLiveEvent<Void>()
    val addResetPasswordFragmentEvent = SingleLiveEvent<Void>()
    var consentManagerId: String = ""
    var sessionId: String? = null
    var tempToken: String? = null

    val redirectTo: SingleLiveEvent<Show> = SingleLiveEvent()

    fun init(){
        redirectTo.value = Show.OTP_VERIFICATION_SCREEN
    }
    fun onVerifyOtpRedirectRequest() {
        redirectTo.value = Show.SET_PASSWORD_SCREEN
    }

}