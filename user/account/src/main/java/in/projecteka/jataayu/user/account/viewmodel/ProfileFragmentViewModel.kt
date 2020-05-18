package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.partialFailure
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import java.util.*

class ProfileFragmentViewModel(val repository: UserAccountsRepository,
                               val preferenceRepository: PreferenceRepository,
                               val credentialsRepository: CredentialsRepository): BaseViewModel() {

    private var yob: Int? = null
    var isEditMode = SingleLiveEvent<Boolean>()
    val logoutResponse = PayloadLiveData<Void>()
    var redirectTo = SingleLiveEvent<RedirectTo>()
    var pinCreateOrEdit = ObservableField<Int>(R.string.create)
    var showPinNotCreated = ObservableBoolean(true)

    companion object {
        private const val INDIA_COUNTRY_CODE = "+91"
        private const val COUNTRY_CODE_SEPARATOR = "-"
        private const val YOB = "yyyy"
        const val KEY_SCOPE_TYPE = "scope_type"
        const val CONSENT_PIN_PLACEHOLDER = "1234"
        const val CONSENT_PIN_EMPTY = ""
    }

    enum class RedirectTo {
        CONSENT_PIN
    }

    fun yearOfBirth(): String{
        val yob = preferenceRepository.yearOfBirth
        if (yob == 0) return ""
        else return yob.toString()
    }

    fun init() {
        isEditMode.value = false
        setConsentPinStatus()
    }

    internal fun getYearsToPopulate(): List<String> {
        val years = arrayListOf<String>(YOB)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (year in currentYear downTo (currentYear - 120)) {
            years.add(year.toString())
        }
        return years
    }

    fun selectedYoB(yob: Int){
        this.yob = yob
    }

    fun logout() {
        credentialsRepository.refreshToken?.let {
            logoutResponse.fetch(repository.logout(it))
        } ?: kotlin.run {
            logoutResponse.partialFailure(null)
        }
    }

    fun clearSharedPreferences() {
        preferenceRepository.resetPreferences()
        credentialsRepository.reset()
    }

    fun redirectToEditConsentPin() {
        redirectTo.value = RedirectTo.CONSENT_PIN
    }

    fun setConsentPinStatus(){
        if (preferenceRepository.pinCreated) {
            pinCreateOrEdit.set(R.string.edit)
            showPinNotCreated.set(false)
        }
    }
}