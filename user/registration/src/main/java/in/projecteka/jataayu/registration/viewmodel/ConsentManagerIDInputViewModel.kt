package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

enum class LoginMode {
    OTP, PASSWORD
}

class ConsentManagerIDInputViewModel(val userAccountsRepository: UserAccountsRepository) : BaseViewModel(), TextWatcher {

    val onRegisterButtonClickEvent = SingleLiveEvent<Void>()
    val onNextButtonClickEvent = SingleLiveEvent<Void>()
    val onForgetCMIDButtonClickEvent = SingleLiveEvent<Void>()

    val cmIDCheckMarkImage = ObservableField<Int>(R.drawable.ic_check_gray)
    val inputUsernameLbl = ObservableField<String>()
    val nextEnabled = ObservableBoolean(false)



    fun onNextButtonClicked() {
        onNextButtonClickEvent.call()
    }

    fun onForgotConsentManagerID() {
        onForgetCMIDButtonClickEvent.call()
    }

    fun onRegisterButtonClicked() {
        onRegisterButtonClickEvent.call()
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        nextEnabled.set(inputUsernameLbl.get()?.isNotEmpty() == true)
    }
}
