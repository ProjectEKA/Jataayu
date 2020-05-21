package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.COUNTRY_CODE_SEPARATOR
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.INDIA_COUNTRY_CODE
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.TYPE_AYUSHMAN_BHARAT_ID
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.TYPE_MOBILE_NUMBER
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.material.chip.ChipGroup
import java.util.*
import java.util.regex.Pattern

class ReadValuesFragmentViewModel(private val repository: UserAccountsRepository,
                             val preferenceRepository: PreferenceRepository,
                             val credentialsRepository: CredentialsRepository) : BaseViewModel(), ChipGroup.OnCheckedChangeListener {

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
        private const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,30}\$"
        const val ayushmanIdCriteria =  "^[P|p]([A-Z][0-9])*.{8}$"
        private const val DEFAULT_CHECKED_ID = -1
        private const val LENGTH_MOBILE_NUMBER = 10
        private const val ERROR_CODE_ = 10
        const val ERROR_CODE_NO_MATCHING_RECORDS = 1000
        const val ERROR_CODE_MULTIPLE_MATCHING_RECORDS = 1001
    }

    private var genderCheckId: Int = -1
    private var selectedYoB: Int? = null

    val inputAyushmanIdLbl = ObservableField<String>()
    val inputFullName = ObservableField<String>()
    val inputMobileNumber = ObservableField<String>()

    val countryCode = ObservableField<String>("$INDIA_COUNTRY_CODE$COUNTRY_CODE_SEPARATOR")

    val submitEnabled = ObservableBoolean(false)

    val showErrorAyushmanId = ObservableBoolean(false)
    val showErrorName = ObservableBoolean(false)
    val showErrorGender = ObservableBoolean(false)
    val showErrorMobile = ObservableBoolean(false)


    val recoverCmidResponse = PayloadLiveData<RecoverCmidResponse>()


    fun validateFields(): Boolean {

        val listOfEvents: List<Boolean> = listOf(
            !showErrorName.get() && inputFullName.get()?.isNotEmpty() == true,
            !showErrorGender.get() && genderCheckId != DEFAULT_CHECKED_ID,
            !showErrorMobile.get() && inputMobileNumber.get()?.isNotEmpty() == true
        )

        var isValid = listOfEvents.all { it }

        inputAyushmanIdLbl.get()?.let {
            if (inputAyushmanIdLbl.get()?.isNotEmpty() == true && showErrorAyushmanId.get()) {
                isValid = false
            }
        }

        submitEnabled.set(isValid)
        return isValid
    }

    fun recoverCmid() {
        recoverCmidResponse.fetch(repository.recoverCmid(getRecoverCmidPayload()))
    }

    fun createAccount() {

//        val payload = getCreateAccountPayload()
//        val call = repository.createAccount(payload)
//        createAccountResponse.fetch(call)
    }

    fun getRecoverCmidPayload(): RecoverCmidRequest {

        var unverifiedIdentifiers: List<UnverifiedIdentifier>? = null
        var verifiedIdentifiers: List<Identifier>? = null

        if (!inputAyushmanIdLbl.get().isNullOrEmpty()){
            if (!showErrorAyushmanId.get()) {
                unverifiedIdentifiers =
                    listOf(UnverifiedIdentifier(inputAyushmanIdLbl.get().toString().toUpperCase(), TYPE_AYUSHMAN_BHARAT_ID))
            }
        }

        if (!inputMobileNumber.get().isNullOrEmpty()){
            if (!showErrorMobile.get()) {
                verifiedIdentifiers =
                    listOf(Identifier(inputMobileNumber.get().toString().toUpperCase(), TYPE_MOBILE_NUMBER, null))
            }
        }

        return RecoverCmidRequest(
            name = inputFullName.get() ?: "",
            gender = getGender(),
            yearOfBirth = selectedYoB,
            verifiedIdentifiers = verifiedIdentifiers,
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

    private fun getGender(): String {
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

    fun validateMobileNumber() {
        showErrorMobile.set(inputMobileNumber.get()?.isEmpty() == true || inputMobileNumber.get()?.length != 10)
        validateFields()
    }

    private fun isValid(text: String, criteria: String): Boolean {
        if(text.isEmpty()) return false
        val pattern = Pattern.compile(criteria)
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun getAuthTokenWithTokenType(response: CreateAccountResponse?): String {
        return "${response?.tokenType?.capitalize()} ${response?.accessToken}"
    }

    override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        genderCheckId = checkedId
        showErrorGender.set(genderCheckId == DEFAULT_CHECKED_ID)
        validateFields()
    }
}