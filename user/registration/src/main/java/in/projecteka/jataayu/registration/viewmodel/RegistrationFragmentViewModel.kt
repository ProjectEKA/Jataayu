package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import `in`.projecteka.jataayu.registration.model.RequestVerificationRequest
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class RegistrationFragmentViewModel : BaseViewModel(), TextWatcher, View.OnFocusChangeListener {

    companion object {

        private const val INDIA_COUNTRY_CODE = "+91"
        private const val COUNTRY_CODE_SEPARATOR = "-"
        private const val MOBILE_IDENTIFIER_TYPE = "mobile"
        private const val MOBILE_DUMMY = "99999 99999"
    }

    val continueButtonEnabled = ObservableBoolean()
    val countryCode = ObservableField<String>("${INDIA_COUNTRY_CODE}$COUNTRY_CODE_SEPARATOR")
    val mobileNumberText = ObservableField<String>()
    val mobileNumberEditTextHint = ObservableField<String>()

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

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(hasFocus){
            mobileNumberEditTextHint.set(MOBILE_DUMMY)
        }else {
            mobileNumberEditTextHint.set("")
        }
    }
}