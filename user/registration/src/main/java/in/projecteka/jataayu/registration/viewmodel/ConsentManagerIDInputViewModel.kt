package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.LoginMode
import `in`.projecteka.jataayu.core.model.LoginType
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.model.APIResponse
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations


class ConsentManagerIDInputViewModel(private val userAccountsRepository: UserAccountsRepository, preferenceRepository: PreferenceRepository) : BaseViewModel(), TextWatcher {

    val onRegisterButtonClickEvent = SingleLiveEvent<Void>()
    val onNextButtonClickEvent = SingleLiveEvent<Void>()
    val onForgetCMIDButtonClickEvent = SingleLiveEvent<Void>()

    val inputUsernameLbl = ObservableField<String>()
    val nextEnabled = ObservableBoolean(false)

   private val loginModeLiveDataResponse = PayloadLiveData<LoginType>()

    val loginMode: LiveData<APIResponse<out LoginMode>?> = Transformations.map(loginModeLiveDataResponse) {
        when(it) {
            is Success -> {
                preferenceRepository.loginMode = it.data?.loginMode?.name
                APIResponse(it.data?.loginMode, null)
            }
            is PartialFailure -> {
                APIResponse(null, it.error)
            }
            is Failure -> {
                APIResponse(null, APIResponse.getError(it.error))
            }
            is Loading -> {
                showProgress(it.isLoading)
                null
            }
        }
    }

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
        loginModeLiveDataResponse.fetch(userAccountsRepository.getLoginMode(cmId))
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        nextEnabled.set(inputUsernameLbl.get()?.isNotEmpty() == true)
    }
}
