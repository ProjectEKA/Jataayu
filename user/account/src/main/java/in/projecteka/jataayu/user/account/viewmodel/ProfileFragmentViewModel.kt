package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.partialFailure
import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

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
        CONSENT_PIN, CHANGE_PASSWORD
    }

    fun yearOfBirth(): String{
        val yob = preferenceRepository.yearOfBirth
        return if (yob == 0) " "
        else yob.toString()
    }

    fun ayushmanBharatId(): String {
        val ayushmanId = preferenceRepository.ayushmanBharatId
        return if (ayushmanId == null) " "
        else ayushmanId
    }

    fun init() {
        isEditMode.value = false
        setConsentPinStatus()
    }

    fun logout() {
        credentialsRepository.refreshToken?.let {
            logoutResponse.fetch(repository.logout(it))
        } ?: kotlin.run {
            logoutResponse.partialFailure(null)
        }
    }

    fun redirectChangePassword(){
        redirectTo.value = RedirectTo.CHANGE_PASSWORD
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
            pinCreateOrEdit.set(R.string.update)
            showPinNotCreated.set(false)
        }
    }

    fun fullName(): String {
        var name = " "

        if (!preferenceRepository.first_name.isNullOrEmpty()) {
            name = preferenceRepository.first_name!!
        }
        if (!preferenceRepository.middle_name.isNullOrEmpty()){
            name = name + " " + preferenceRepository.middle_name
        }
        if (!preferenceRepository.last_name.isNullOrEmpty()){
            name = name + " " + preferenceRepository.last_name
        }
        return name
    }
}