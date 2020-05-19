package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.network.utils.partialFailure
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.databinding.ObservableBoolean
import java.util.*

class ProfileFragmentViewModel(val repository: UserAccountsRepository,
                               val preferenceRepository: PreferenceRepository,
                               val credentialsRepository: CredentialsRepository): BaseViewModel() {

    private var yob: Int? = null
    var isEditMode = SingleLiveEvent<Boolean>()
    val logoutResponse = PayloadLiveData<Void>()
    var redirectTo = SingleLiveEvent<RedirectTo>()
    val editPasswordEnabled = ObservableBoolean()

    enum class RedirectTo {
        CHANGE_PASSWORD
    }

    companion object {
        private const val INDIA_COUNTRY_CODE = "+91"
        private const val COUNTRY_CODE_SEPARATOR = "-"
        private const val YOB = "yyyy"
    }

    fun yearOfBirth(): String{
        val yob = preferenceRepository.yearOfBirth
        if (yob == 0) return ""
        else return yob.toString()
    }

    fun init() {
        isEditMode.value = false
        var loginMode =preferenceRepository.loginMode == "OTP"
        editPasswordEnabled.set(!loginMode)
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

    fun redirectChangePassword(){
        redirectTo.value = RedirectTo.CHANGE_PASSWORD
    }

    fun clearSharedPreferences() {
        preferenceRepository.resetPreferences()
        credentialsRepository.reset()
    }
}