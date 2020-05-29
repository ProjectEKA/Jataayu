package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.LoginMode
import `in`.projecteka.jataayu.core.model.LoginType
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField


class ConsentManagerIDInputViewModel(private val userAccountsRepository: UserAccountsRepository, private val preferenceRepository: PreferenceRepository) : BaseViewModel(), TextWatcher {

    val onRegisterButtonClickEvent = SingleLiveEvent<Void>()
    val onNextButtonClickEvent = SingleLiveEvent<Void>()
    val onForgetCMIDButtonClickEvent = SingleLiveEvent<Void>()

    val inputUsernameLbl = ObservableField<String>()
    val providerName = ObservableField<String>()
    val nextEnabled = ObservableBoolean(false)

    //avoid on change call on back button press
    var isLoginModeHasLoaded: Boolean = false
    private set


    val loginModeLiveDataResponse = PayloadLiveData<LoginType>()


    fun onNextButtonClicked() {
        onNextButtonClickEvent.call()
    }

    fun onForgotConsentManagerID() {
        onForgetCMIDButtonClickEvent.call()
    }

    fun onRegisterButtonClicked() {
        onRegisterButtonClickEvent.call()
    }

    fun fetchLoginMode(cmId: String) {
        isLoginModeHasLoaded = false
        loginModeLiveDataResponse.fetch(userAccountsRepository.getLoginMode(cmId))
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        nextEnabled.set(inputUsernameLbl.get()?.isNotEmpty() == true)
    }

    fun init(provider: String) {
        providerName.set(provider)
    }

     fun onLoginModeResponseSuccess(loginMode: LoginMode) {
         preferenceRepository.loginMode = loginMode.mode
         isLoginModeHasLoaded = true
    }
}
