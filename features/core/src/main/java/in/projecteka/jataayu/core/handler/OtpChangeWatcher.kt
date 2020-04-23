package `in`.projecteka.jataayu.core.handler

import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.IntRange

class OtpChangeWatcher(@IntRange(from = 0L, to = 6L) private val otpLength: Int, private val otpChangeListener: OtpChangeHandler) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
            }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        otpChangeListener.setButtonEnabled(s?.length == otpLength)
    }
}