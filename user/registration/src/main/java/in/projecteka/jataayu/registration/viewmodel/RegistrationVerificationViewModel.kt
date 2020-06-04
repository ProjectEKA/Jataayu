package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class RegistrationVerificationViewModel : BaseViewModel(), TextWatcher {

    companion object {
        const val OTP_LENGTH = 6
        private val MOBILE_IDENTIFIER_TYPE = "mobile"
        private val ERROR_EMPTY = ""
    }

    val errorLbl = ObservableField<String>()
    val otpText = ObservableField<String>()
    val mobileNumberText = ObservableField<String>()
    val submitEnabled = ObservableBoolean()

    val onClickVerifyEvent = SingleLiveEvent<String>()
    val onClickResendEvent = SingleLiveEvent<RequestVerificationRequest>()

    override fun afterTextChanged(s: Editable?) {
        if (otpText.get()?.isNotEmpty() == true) {
            errorLbl.set(ERROR_EMPTY)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        submitEnabled.set(s?.length == OTP_LENGTH)
    }

    fun onClickVerify() {
        onClickVerifyEvent.value = otpText.get()
    }

    fun onClickResend(){
        errorLbl.set(ERROR_EMPTY)
        onClickResendEvent.value = RequestVerificationRequest(MOBILE_IDENTIFIER_TYPE, mobileNumberText.get().toString())
    }
}