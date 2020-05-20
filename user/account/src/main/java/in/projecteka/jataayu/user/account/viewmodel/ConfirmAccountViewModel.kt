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
import androidx.databinding.ObservableInt
import java.util.regex.Pattern

class ConfirmAccountViewModel(private val repository: UserAccountsRepository,
                             val preferenceRepository: PreferenceRepository,
                             val credentialsRepository: CredentialsRepository) : BaseViewModel(){

    companion object {

//        private const val YOB = "yyyy"
//        private const val usernameCriteria = "^[a-zA-Z0-9.-]{3,150}$"
        /*^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string*/
        private const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,30}\$"
//        const val ayushmanIdCriteria =  "^[P|p]([A-Z][0-9])*.{8}$"
//        private const val DEFAULT_CHECKED_ID = -1
    }

    private var genderCheckId: Int = -1
    private var selectedYoB: Int? = null

    val inputUsernameLbl = ObservableField<String>()
    val inputPasswordLbl = ObservableField<String>()
    val confirmationInputPasswordLbl = ObservableField<String>()
    val passwordInputType = ObservableInt(hiddenPasswordInputType())
    val inputPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val usernameProviderLbl = ObservableField<String>()
    val usernameProviderLblId = ObservableField<Int>(R.string.ncg)
    val inputAyushmanIdLbl = ObservableField<String>()
    val submitEnabled = ObservableBoolean(false)
    val showErrorUserName = ObservableBoolean(false)
    val showErrorPassword = ObservableBoolean(false)
    val showErrorAyushmanId = ObservableBoolean(false)
    val onPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val createAccountResponse = PayloadLiveData<CreateAccountResponse>()


    fun validateFields(): Boolean {

        val listOfEvents: List<Boolean> = listOf(
            !showErrorUserName.get() && inputUsernameLbl.get()?.isNotEmpty() == true,
            !showErrorPassword.get() && inputPasswordLbl.get()?.equals(confirmationInputPasswordLbl.get())!!,
            !isValid(inputPasswordLbl.get().toString(), passwordCriteria)
        )

        var isValid = listOfEvents.all { it == true }
        submitEnabled.set(isValid)
        return isValid
    }

    fun togglePasswordVisible() {
        val newInputType = when (passwordInputType.get()) {
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
        passwordInputType.set(newInputType)
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
        val payload = getCreateAccountPayload()
        val call = repository.createAccount(payload)
        createAccountResponse.fetch(call)

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
//            name = inputFullName.get() ?: "",
            name = "",
//            gender = getGender(),
            gender = "M",
            yearOfBirth = selectedYoB,
            unverifiedIdentifiers = unverifiedIdentifiers)
    }

}