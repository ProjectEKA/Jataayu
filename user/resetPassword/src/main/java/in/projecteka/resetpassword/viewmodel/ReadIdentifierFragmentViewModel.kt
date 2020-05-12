package `in`.projecteka.resetpassword.viewmodel

import `in`.projecteka.forgotpassword.R
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.resetpassword.model.GenerateOTPRequest
import `in`.projecteka.resetpassword.model.GenerateOTPResponse
import `in`.projecteka.resetpassword.repository.ResetPasswordRepository
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class ReadIdentifierFragmentViewModel(val resetPasswordRepository: ResetPasswordRepository): BaseViewModel() {


    val consentManagerIdProviderLblId = ObservableField<Int>(R.string.cm_config_provider)
    val consentManagerIdProviderLbl = ObservableField<String>()
    val setEnableButton = ObservableBoolean(false)

    val showErrorConsentManagerId = ObservableBoolean(false)
    val generateOtpResponse = PayloadLiveData<GenerateOTPResponse>()
    val consentManagerIdField = MutableLiveData<String>()
    val inputConsentManagerId = ObservableField<String>()

    companion object {
        private const val usernameCriteria = "^[a-zA-Z0-9.-]{3,150}$"
    }

    fun validateConsentManagerId(consentManagerId: String) {
        consentManagerId.let {
            showErrorConsentManagerId.set(!isValid(it,
                usernameCriteria
            ))
        }

        consentManagerIdField.value = consentManagerId + consentManagerIdProviderLbl.get()
        setEnableButton.set(!showErrorConsentManagerId.get() && consentManagerId?.isNotEmpty())
    }

    fun generateOtp() {
        generateOtpResponse.fetch(resetPasswordRepository.generateOtp(GenerateOTPRequest(consentManagerIdField.value)))
    }

    private fun isValid(text: String, criteria: String): Boolean {
        if (text.isEmpty()) return false
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }
}