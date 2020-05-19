package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.registration.model.*
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class LoginOtpViewModel(val authenticationRepository: AuthenticationRepository,  private val credentialsRepository: CredentialsRepository, private val preferenceRepository: PreferenceRepository) : BaseViewModel(), TextWatcher {

    companion object {
        const val OTP_LENGTH = 6
        private val MOBILE_IDENTIFIER_TYPE = "mobile"
    }

    val loginByOTPResponseLiveData = PayloadLiveData<CreateAccountResponse>()
    val otpSessionResponseLiveData = PayloadLiveData<LoginOTPSessionResponse>()
    val otpMessageLbl = ObservableField<CharSequence>()
    val errorLbl = ObservableField<String>()
    val otpText = ObservableField<String>()
    val mobileNumberText = ObservableField<String>()
    val continueEnabled = ObservableBoolean()

    val accountLockBlockEnable = ObservableField<Int>(View.GONE)
    val accountLockBlockDividerEnable = ObservableField<Int>(View.GONE)

    val onClickResendEvent = SingleLiveEvent<RequestVerificationRequest>()
    val timeRemaining = ObservableField<String>("01:59")

    private lateinit var cmId: String
    private lateinit var otpSessionResponse: LoginOTPSessionResponse


    override fun afterTextChanged(s: Editable?) {
        if (otpText.get()?.isNotEmpty() == true) {
            errorLbl.set("")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        continueEnabled.set(s?.length == OTP_LENGTH)
    }


    fun generateOtp(userName : String) {
        otpSessionResponseLiveData.fetch(authenticationRepository.generateOtp(LoginOTPSessionRequest(userName)))
    }

    fun verifyLoginOtp(otp: String) {
        loginByOTPResponseLiveData.fetch(authenticationRepository.loginOtp(LoginOTPRequest( cmId,otpSessionResponse.sessionId, otp)))
    }

    fun onClickResend(){
        onClickResendEvent.value = RequestVerificationRequest(MOBILE_IDENTIFIER_TYPE, mobileNumberText.get().toString())
    }

    fun onOtpSessionCreateSuccess(cmId: String, otpSessionResponse: LoginOTPSessionResponse) {
        this.cmId = cmId
        this.otpSessionResponse = otpSessionResponse
    }

    fun onLoginSuccess(response: CreateAccountResponse) {
        credentialsRepository.accessToken =
            "${response.tokenType.capitalize()} ${response.accessToken}"
        preferenceRepository.isUserLoggedIn = true
        credentialsRepository.refreshToken = response.refreshToken
    }

}
