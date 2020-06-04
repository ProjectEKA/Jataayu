package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class CreatePinFragmentViewModel() : BaseViewModel(), TextWatcher {
    val continueEnabled = ObservableBoolean(false)
    val inputPinLbl = ObservableField<String>()
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("inputUsernameLbl","inputUsernameLbl.get()?.length : " + inputPinLbl.get()?.length)
        continueEnabled.set(s?.length == PreferenceRepository.TRANSACTION_PIN_LENGTH)
    }
}

