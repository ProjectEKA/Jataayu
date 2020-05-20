package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.model.UnverifiedIdentifier
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.TYPE_AYUSHMAN_BHARAT_ID
import android.text.InputType
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.google.android.material.chip.ChipGroup
import java.util.*
import java.util.regex.Pattern

class CreateAccountViewModel : BaseViewModel(), ChipGroup.OnCheckedChangeListener {

    companion object {
        private const val YOB = "yyyy"
        private const val usernameCriteria = "^[a-zA-Z0-9.-]{3,150}$"
        /*^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string*/
//        private const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,30}\$"
        const val ayushmanIdCriteria =  "^[P|p]([A-Z][0-9])*.{8}$"
        private const val DEFAULT_CHECKED_ID = -1
    }

    private var genderCheckId: Int = -1
    internal var selectedYoB: Int? = null
    val inputFullName = ObservableField<String>()
    val inputAyushmanIdLbl = ObservableField<String>()
    val showErrorName = ObservableBoolean(false)
    val showErrorAyushmanId = ObservableBoolean(false)
    val showErrorGender = ObservableBoolean(false)
    val submitEnabled = ObservableBoolean(false)
    val redirectTo: SingleLiveEvent<AccountCreationActivityViewModel.ShowPage> = SingleLiveEvent()
    val inputUsernameLbl = ObservableField<String>()
    val inputPasswordLbl = ObservableField<String>()
    val usernameProviderLbl = ObservableField<String>()
    val showErrorPassword = ObservableBoolean(false)
    val showErrorUserName = ObservableBoolean(false)
    val confirmationInputPasswordLbl = ObservableField<String>()
    val passwordInputType = ObservableInt(hiddenPasswordInputType())
    val inputPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val usernameProviderLblId = ObservableField<Int>(R.string.ncg)
    val onPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()


    fun validateFields(): Boolean {

        val listOfEvents: List<Boolean> = listOf(
            !showErrorName.get() && inputFullName.get()?.isNotEmpty() == true,
            !showErrorGender.get() && genderCheckId != DEFAULT_CHECKED_ID
        )

        var isValid = listOfEvents.all { it == true }

        inputAyushmanIdLbl.get()?.let {
            if (inputAyushmanIdLbl.get()?.isNotEmpty() == true && showErrorAyushmanId.get()) {
                isValid = false
            }
        }

        submitEnabled.set(isValid)
        return isValid
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
            gender = getGender(),
            yearOfBirth = selectedYoB,
            unverifiedIdentifiers = unverifiedIdentifiers)
    }

    internal fun getYearsToPopulate(): List<String> {
        val years = arrayListOf<String>(YOB)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (year in currentYear downTo (currentYear - 120)) {
            years.add(year.toString())
        }
        return years
    }

    internal fun getGender(): String {
        return when (genderCheckId) {
            R.id.gender_chip_male -> PreferenceRepository.GENDER_MALE
            R.id.gender_chip_female -> PreferenceRepository.GENDER_FEMALE
            R.id.gender_chip_other -> PreferenceRepository.GENDER_OTHERS
            else -> PreferenceRepository.GENDER_OTHERS
        }
    }

    fun selectedYoB(yob: Int) {
        selectedYoB = yob
    }

    fun validateName() {
        showErrorName.set(inputFullName.get()?.isEmpty() == true)
        validateFields()
    }

    fun validateAyushmanId(){
        if (inputAyushmanIdLbl.get()?.isNotEmpty() == true){
            inputAyushmanIdLbl.get()?.let {
                showErrorAyushmanId.set(!isValid(it, ayushmanIdCriteria))
            }
        }
        validateFields()
    }

    private fun isValid(text: String, criteria: String): Boolean {
        if(text.isEmpty()) return false
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        genderCheckId = checkedId
        showErrorGender.set(genderCheckId == DEFAULT_CHECKED_ID)
        validateFields()
    }
    private fun hiddenPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
}