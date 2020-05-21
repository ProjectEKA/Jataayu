package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.model.UnverifiedIdentifier
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.TYPE_AYUSHMAN_BHARAT_ID
import android.text.InputType
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.material.chip.ChipGroup
import java.util.regex.Pattern

class ConfirmAccountViewModel(private val repository: UserAccountsRepository,
                             val preferenceRepository: PreferenceRepository,
                             val credentialsRepository: CredentialsRepository) : BaseViewModel(){

    companion object {
        private const val usernameCriteria = "^[a-zA-Z0-9.-]{3,150}$"
        private const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,30}\$"
        private const val DEFAULT_CHECKED_ID = -1
    }

    private var genderCheckId: Int = -1
    val inputUsernameLbl = ObservableField<String>()
    val inputPasswordLbl = ObservableField<String>()
    val confirmationInputPasswordLbl = ObservableField<String>()
    val passwordInputType = ObservableField(hiddenPasswordInputType())
    val confirmationPasswordInputType = ObservableField(hiddenPasswordInputType())
    val inputPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val usernameProviderLbl = ObservableField<String>()
    val usernameProviderLblId = ObservableField<Int>(R.string.ncg)
    val inputAyushmanIdLbl = ObservableField<String>()
    val submitEnabled = ObservableBoolean(true)
    val showErrorUserName = ObservableBoolean(false)
    val showErrorGender = ObservableBoolean(false)
    val showErrorPassword = ObservableBoolean(false)
    val showErrorConfirmPassword = ObservableBoolean(false)
    val showErrorAyushmanId = ObservableBoolean(false)
    val onPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val createAccountResponse = PayloadLiveData<CreateAccountResponse>()
    var inputFullName = ObservableField<String>()
    var inputGender = ObservableField<String>()
    var selectedYoB = ObservableField<Int>()
    val showErrorName = ObservableBoolean(false)


    fun validatePassword() {
        if(inputPasswordLbl.get()?.isNotEmpty() == true)
            inputPasswordLbl.get()?.let { showErrorPassword.set(!isValid(it, passwordCriteria)) }
        validateFields()
    }
    fun validateConfirmPassword() {
        val arePasswordEqual = (inputPasswordLbl.get() == confirmationInputPasswordLbl.get())
        showErrorConfirmPassword.set(!arePasswordEqual)
    }

    fun validateUserName() {
        if(inputUsernameLbl.get()?.isNotEmpty() == true)
            inputUsernameLbl.get()?.let { showErrorUserName.set(!isValid(it, usernameCriteria)) }
        validateFields()
    }


    fun validateFields(): Boolean {
        val listOfEvents: List<Boolean> = listOf(
            !showErrorUserName.get() && inputUsernameLbl.get()?.isNotEmpty() == true,
            !showErrorPassword.get() && inputPasswordLbl.get()?.equals(confirmationInputPasswordLbl.get())!!
        )

        var isValid = listOfEvents.all { it == true }
        return isValid
    }

    fun togglePasswordVisible() {
        val newInputType = when (confirmationPasswordInputType.get()) {
            visiblePasswordInputType() -> {
                inputPasswordVisibilityToggleLbl.set(R.string.show)
                hiddenPasswordInputType()
            }
            hiddenPasswordInputType() -> {
                inputPasswordVisibilityToggleLbl.set(R.string.hide)
                visiblePasswordInputType()
            }
            else -> hiddenPasswordInputType()
        }
        confirmationPasswordInputType.set(newInputType)
        onPasswordVisibilityToggleEvent.value = inputPasswordLbl.get()?.length ?: 0
    }
    private fun visiblePasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    private fun hiddenPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun isValid(text: String, criteria: String): Boolean {
        if(text.isEmpty()) return false
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }


    fun createAccount() {
        if (validateFields()){
            val payload = getCreateAccountPayload()
            println(payload)
            val call = repository.createAccount(payload)
            print(call)
            val fetch = createAccountResponse.fetch(call)
            print(fetch)
        }
    }

    fun getAuthTokenWithTokenType(response: CreateAccountResponse?): String {
        return "${response?.tokenType?.capitalize()} ${response?.accessToken}"
    }

    fun getCreateAccountPayload(): CreateAccountRequest {

        var unverifiedIdentifiers: List<UnverifiedIdentifier>? = null
        if (!inputAyushmanIdLbl.get().isNullOrEmpty()){
            if (!showErrorAyushmanId.get()) {
                unverifiedIdentifiers =
                    listOf(UnverifiedIdentifier(inputAyushmanIdLbl.get().toString().toUpperCase(), TYPE_AYUSHMAN_BHARAT_ID))
            }
        }

        return CreateAccountRequest(
            userName = "${inputUsernameLbl.get()}${usernameProviderLbl.get()}" ,
            password = inputPasswordLbl.get() ?: "",
            name = inputFullName.get() ?: "",
            gender = inputGender.get() ?: "",
            yearOfBirth = (selectedYoB.get() ?: "") as Int,
            unverifiedIdentifiers = unverifiedIdentifiers)
    }
    fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        genderCheckId = checkedId
        showErrorGender.set(genderCheckId == ConfirmAccountViewModel.DEFAULT_CHECKED_ID)
        validateFields()
    }

}