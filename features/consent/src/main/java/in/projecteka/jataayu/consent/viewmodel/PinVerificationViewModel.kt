package `in`.projecteka.jataayu.consent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PinVerificationViewModel() : ViewModel() {
    enum class EVENT_TYPE {
        USER_VERIFIED
    }

    var event = MutableLiveData<EVENT_TYPE>()
}

