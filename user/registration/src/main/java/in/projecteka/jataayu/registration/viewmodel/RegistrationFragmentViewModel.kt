package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.COUNTRY_CODE_SEPARATOR
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.INDIA_COUNTRY_CODE
import `in`.projecteka.jataayu.util.repository.PreferenceRepository.Companion.MOBILE_IDENTIFIER_TYPE
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class RegistrationFragmentViewModel : BaseViewModel(), TextWatcher {

    companion object {

    }

    val continueButtonEnabled = ObservableBoolean()
    val countryCode = ObservableField<String>("${INDIA_COUNTRY_CODE}$COUNTRY_CODE_SEPARATOR")
    val mobileNumberText = ObservableField<String>()

    val onContinueClicked = SingleLiveEvent<RequestVerificationRequest>()

    fun onContinueClicked() {
        onContinueClicked.value = RequestVerificationRequest(MOBILE_IDENTIFIER_TYPE, "${getCountryCode()}${mobileNumberText.get()}")
    }


    fun getCountryCode() = "${INDIA_COUNTRY_CODE}${COUNTRY_CODE_SEPARATOR}"

    override fun afterTextChanged(s: Editable?) {
        //do nothing
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        continueButtonEnabled.set(s?.length == 10)
    }
}