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

    val otpMessageLbl = ObservableField<String>()
    val errorLbl = ObservableField<String>()
    val otpText = ObservableField<String>()
    val mobileNumberText = ObservableField<String>()
    val submitEnabled = ObservableBoolean()

    val onClickVerifyEvent = SingleLiveEvent<String>()

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        errorLbl.set("")
        submitEnabled.set(s?.length == OTP_LENGTH)
    }

    fun onClickVerify() {
        onClickVerifyEvent.value = otpText.get()
    }

    //    fun requestVerification(identifierType: String, identifier: String, responseCallback: ResponseCallback) {
//        repository.requestVerification(RequestVerificationRequest(identifierType, identifier)).observeOn(requestVerificationResponse, responseCallback)
//    }

//    fun verifyIdentifier(sessionId: String, otp: String, responseCallback: ResponseCallback) {
//        repository.verifyIdentifier(VerifyIdentifierRequest(sessionId, otp)).observeOn(verifyIdentifierResponse, responseCallback)
//    }

//    fun getMobileNumber(mobileNumber: String): String {
////        return getCountryCode() + mobileNumber
//    }

//    fun getCountryCode(): String? {
//        return INDIA_COUNTRY_CODE + COUNTRY_CODE_SEPARATOR
//    }


}