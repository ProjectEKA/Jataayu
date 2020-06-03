package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.model.ChangePasswordRequest
import `in`.projecteka.jataayu.user.account.model.ChangePasswordResponse
import `in`.projecteka.jataayu.user.account.repository.ChangePasswordRepository
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.text.InputType
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import java.util.regex.Pattern

class ChangePasswordViewModel(val changePasswordRepository: ChangePasswordRepository): BaseViewModel() {
    val inputConfirmPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val inputOldPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val oldPasswordInputType = ObservableInt(hiddenPasswordInputType())
    val createPasswordInputType = ObservableInt(hiddenPasswordInputType())
    val confirmPasswordInputType = ObservableInt(hiddenPasswordInputType())
    val onConfirmPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val onOldPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val inputOldPasswordLbl = ObservableField<String>()
    val inputCreatePasswordLbl = ObservableField<String>()
    val inputConfirmPasswordLbl = ObservableField<String>()
    val showInvalidOldPasswordError = ObservableField<String>()
    val setConfirmButtonEnabled = ObservableBoolean(false)
    val showErrorCreatePassword = ObservableBoolean(false)
    val showErrorOldPassword = ObservableBoolean(false)
    val showConfirmPasswordMismatch = ObservableBoolean(false)
    val showConfirmPasswordMatch = ObservableBoolean(false)

    val changePasswordResponse = PayloadLiveData<ChangePasswordResponse>()

    private var tempToken: String? = null

    companion object {
        private const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,30}\$"
    }

    fun init(tempToken: String) {
        this.tempToken = tempToken
    }

    private fun hiddenPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun visiblePasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    fun validateOldPassword() {

        inputOldPasswordLbl.get()?.let {
            if (inputOldPasswordLbl.get()?.isNotEmpty() == true) {
                showErrorOldPassword.set(it.isNullOrEmpty())
            }
        }
    }

    fun validatePassword() {
        if(inputCreatePasswordLbl.get()?.isNotEmpty() == true) {
            inputCreatePasswordLbl.get()?.let { showErrorCreatePassword.set(!isValidPassword(it, passwordCriteria))}
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

    private fun isValidPassword(text: String, criteria: String): Boolean {
        if(text.isEmpty()) return false
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }


    fun changePassword() {
        changePasswordResponse.fetch(changePasswordRepository.changePassword(ChangePasswordRequest(inputOldPasswordLbl.get().toString(),inputConfirmPasswordLbl.get().toString())))
    }
}