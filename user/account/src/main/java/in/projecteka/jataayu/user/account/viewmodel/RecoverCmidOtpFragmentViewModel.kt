package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class RecoverCmidOtpFragmentViewModel : BaseViewModel(), TextWatcher {


    companion object {
        const val OTP_LENGTH = 6
        private val MOBILE_IDENTIFIER_TYPE = "mobile"
    }

    val otpMessageLbl = ObservableField<CharSequence>()
    val errorLbl = ObservableField<String>()
    val otpText = ObservableField<String>()
    val mobileNumberText = ObservableField<String>()
    val continueEnabled = ObservableBoolean()

    val accountLockBlockEnable = ObservableField<Int>(View.GONE)
    val accountLockBlockDividerEnable = ObservableField<Int>(View.GONE)

    val timeRemaining = ObservableField<String>("01:59")


    fun generateOtp(userName : String) {

    }

    fun verifyLoginOtp(otp: String) {

    }

    fun onClickResend(){

    }

    override fun afterTextChanged(s: Editable?) {
        if (otpText.get()?.isNotEmpty() == true) {
            errorLbl.set("")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        continueEnabled.set(s?.length == OTP_LENGTH)
    }
}
