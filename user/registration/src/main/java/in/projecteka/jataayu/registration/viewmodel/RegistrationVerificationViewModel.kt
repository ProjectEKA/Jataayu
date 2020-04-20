package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class RegistrationVerificationViewModel : ViewModel(), TextWatcher {

    companion object {
        const val OTP_LENGTH = 6
    }

    val otpMessageLbl = ObservableField<CharSequence>()
    val errorLbl = ObservableField<String>()
    val otpText = ObservableField<String>()
    val mobileNumberText = ObservableField<String>()
    val submitEnabled = ObservableBoolean()

    val onClickVerifyEvent = SingleLiveEvent<String>()

    override fun afterTextChanged(s: Editable?) {
        if (otpText.get()?.isNotEmpty() == true) {
            errorLbl.set("")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        submitEnabled.set(s?.length == OTP_LENGTH)
    }

    fun onClickVerify() {
        onClickVerifyEvent.value = otpText.get()
    }

}