package `in`.projecteka.resetpassword.viewmodel

import `in`.projecteka.forgotpassword.R
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.resetpassword.model.ResetPasswordRequest
import `in`.projecteka.resetpassword.model.ResetPasswordResponse
import `in`.projecteka.resetpassword.repository.ResetPasswordRepository
import android.text.InputType
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import java.util.regex.Pattern

class ResetPasswordFragmentViewModel(val resetPasswordRepository: ResetPasswordRepository): BaseViewModel() {
    val inputCreatePasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val inputConfirmPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val createPasswordInputType = ObservableInt(hiddenPasswordInputType())
    val confirmPasswordInputType = ObservableInt(hiddenPasswordInputType())
    val onCreatePasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val onConfirmPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val inputCreatePasswordLbl = ObservableField<String>()
    val inputConfirmPasswordLbl = ObservableField<String>()
    val setConfirmButtonEnabled = ObservableBoolean(false)
    val showErrorCreatePassword = ObservableBoolean(false)
    val showConfirmPasswordMismatch = ObservableBoolean(false)
    val showConfirmPasswordMatch = ObservableBoolean(false)

    val resetPasswordResponse = PayloadLiveData<ResetPasswordResponse>()

    private var tempToken: String? = null

    companion object {
        private const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,30}\$"
    }


    fun init(tempToken: String) {
        this.tempToken = tempToken
    }

    fun toggleCreatePasswordVisible() {
        val newInputType = when (createPasswordInputType.get()) {
            visiblePasswordInputType() -> {
                inputCreatePasswordVisibilityToggleLbl.set(R.string.show)
                hiddenPasswordInputType()
            }
            hiddenPasswordInputType() -> {
                inputCreatePasswordVisibilityToggleLbl.set(R.string.hide)
                visiblePasswordInputType()
            }
            else -> hiddenPasswordInputType()
        }
        createPasswordInputType.set(newInputType)
        onCreatePasswordVisibilityToggleEvent.value = inputCreatePasswordLbl.get()?.length ?: 0
    }

    fun toggleConfirmPasswordVisible() {
        val newInputType = when (confirmPasswordInputType.get()) {
            visiblePasswordInputType() -> {
                inputConfirmPasswordVisibilityToggleLbl.set(R.string.show)
                hiddenPasswordInputType()
            }
            hiddenPasswordInputType() -> {
                inputConfirmPasswordVisibilityToggleLbl.set(R.string.hide)
                visiblePasswordInputType()
            }
            else -> hiddenPasswordInputType()
        }
        confirmPasswordInputType.set(newInputType)
        onConfirmPasswordVisibilityToggleEvent.value = inputConfirmPasswordLbl.get()?.length ?: 0
    }

    private fun hiddenPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun visiblePasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    fun validatePassword() {
        if(inputCreatePasswordLbl.get()?.isNotEmpty() == true) {
            inputCreatePasswordLbl.get()?.let { showErrorCreatePassword.set(!isValid(it, passwordCriteria))
            }

            if (showErrorCreatePassword.get()){
                showConfirmPasswordMismatch.set(false)
                showConfirmPasswordMatch.set(false)
                setConfirmButtonEnabled.set(false)
            } else {
                if (inputConfirmPasswordLbl.get()?.isNotEmpty() == true) validateConfirmPassword()
            }
        } else {
            showErrorCreatePassword.set(false)
            showConfirmPasswordMismatch.set(false)
            showConfirmPasswordMatch.set(false)
            setConfirmButtonEnabled.set(false)
        }
    }

    private fun validateConfirmPassword() {
        showConfirmPasswordMismatch.set(inputCreatePasswordLbl.get() != inputConfirmPasswordLbl.get())
        showConfirmPasswordMatch.set(inputCreatePasswordLbl.get() == inputConfirmPasswordLbl.get())
        setConfirmButtonEnabled.set(inputCreatePasswordLbl.get() == inputConfirmPasswordLbl.get())
    }

    private fun isValid(text: String, criteria: String): Boolean {
        if(text.isEmpty()) return false
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun resetPassword() {
        resetPasswordResponse.fetch(resetPasswordRepository.resetPassword(ResetPasswordRequest(inputConfirmPasswordLbl.get().toString()), tempToken?:""))
    }
}