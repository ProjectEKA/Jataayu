package `in`.org.projecteka.jataayu.registration.listener

import android.text.Editable
import android.text.TextWatcher

class MobileNumberChangeWatcher(private val mobileNumberChangeHandler: MobileNumberChangeHandler) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mobileNumberChangeHandler.setButtonEnabled(s?.length == 10)
    }
}