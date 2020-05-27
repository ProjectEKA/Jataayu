package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.GenerateOTPResponse
import `in`.projecteka.jataayu.core.model.RecoverCmidRequest
import `in`.projecteka.jataayu.core.model.RecoverCmidResponse
import `in`.projecteka.jataayu.core.model.VerifyOTPRequest
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.user.account.R
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class RecoverCmidOtpFragmentViewModel(private val repository: UserAccountsRepository) : BaseViewModel(), TextWatcher {


    companion object {
        const val OTP_LENGTH = 6
        private const val ERROR_CODE_INVALID_OTP = 1003
        private const val ERROR_CODE_OTP_EXPIRED = 1004
        private const val EXCEEDED_INVALID_ATTEMPT_LIMIT = 1035
    }

    val errorLbl = ObservableField<String>()
    val otpText = ObservableField<String>()
    val validateButtonEnabled = ObservableBoolean()

    val verifyOTPResponseLiveData = PayloadLiveData<RecoverCmidResponse>()
    val recoverCmidResponseLiveData = PayloadLiveData<GenerateOTPResponse>()

    val timeRemaining = ObservableField<String>("01:59")



    fun verifyOtp(sessionId: String) {
        verifyOTPResponseLiveData.fetch(repository.recoverCmid(getVerifyOTPRequestPayload(sessionId)))
    }

    fun getVerifyOTPRequestPayload(sessionId: String): VerifyOTPRequest {
        return VerifyOTPRequest(sessionId, otpText.get())
    }

    fun resendOTP(recoverCMIDRequest: RecoverCmidRequest) {
        recoverCmidResponseLiveData.fetch(repository.generateOTPForRecoverCMID(recoverCMIDRequest))
    }

    override fun afterTextChanged(s: Editable?) {
        if (otpText.get()?.isNotEmpty() == true) {
            errorLbl.set("")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        validateButtonEnabled.set(s?.length == OTP_LENGTH)
    }

    fun onVerifyOTPResponseFailure(error: Error, resources: Resources) {
        if (error.code == ERROR_CODE_INVALID_OTP ||error.code == ERROR_CODE_OTP_EXPIRED || error.code == EXCEEDED_INVALID_ATTEMPT_LIMIT) {
            otpText.set(null)
        }
        errorLbl.set(
            when (error.code) {
                ERROR_CODE_INVALID_OTP -> {
                    resources.getString(R.string.invalid_otp)
                }
                ERROR_CODE_OTP_EXPIRED -> {
                    resources.getString(R.string.otp_expired)
                }
                EXCEEDED_INVALID_ATTEMPT_LIMIT -> {
                    resources.getString(R.string.exceeded_otp_attempt_limit)
                }
                else -> error.message
            }
        )
    }
}
