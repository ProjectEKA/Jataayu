package `in`.org.projecteka.jataayu.registration.listener

import android.text.Editable
import android.text.TextWatcher

class MobileNumberChangeWatcher(private val mobileNumberChangeListener: MobileNumberChangeHandler) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mobileNumberChangeListener.setButtonEnabled(s?.length == 10)
    }
}