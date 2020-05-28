package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.GenerateOTPResponse
import `in`.projecteka.jataayu.core.model.RecoverCmidRequest
import `in`.projecteka.jataayu.core.model.RecoverCmidResponse
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent

class RecoverCmidActivityViewModel: BaseViewModel() {

    var generateOTPResponse: GenerateOTPResponse? = null
    private set
    var recoverCmidResponse: RecoverCmidResponse? = null
        private set
    // use this for resending otp
    var otpRequest: RecoverCmidRequest? = null
    private set

    enum class Show{
        READ_VALUES_SCREEN,
        OTP_SCREEN,
        DISPLAY_CMID_SCREEN,
        NO_OR_MULTIPLE_MATCHING_RECORDS,
    }

    val redirectTo: SingleLiveEvent<Show> = SingleLiveEvent()

    fun init(){
        redirectTo.value = Show.READ_VALUES_SCREEN
    }

    fun onDisplayCmidRequest(recoverCmidResponse: RecoverCmidResponse) {
        this.recoverCmidResponse = recoverCmidResponse
        redirectTo.value = Show.DISPLAY_CMID_SCREEN
    }

    fun onReviewRequest() {
        redirectTo.value = Show.NO_OR_MULTIPLE_MATCHING_RECORDS
    }

    fun onOTPRequest(otpRequest:RecoverCmidRequest, generateOTPResponse: GenerateOTPResponse) {
        this.generateOTPResponse = generateOTPResponse
        this.otpRequest = otpRequest
        redirectTo.value = Show.OTP_SCREEN
    }
}