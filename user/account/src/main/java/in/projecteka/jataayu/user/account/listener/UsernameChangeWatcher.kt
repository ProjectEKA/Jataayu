package `in`.projecteka.jataayu.user.account.listener

import android.text.Editable
import android.text.TextWatcher

class UsernameChangeWatcher(private val credentialsInputListener: CredentialsInputListener): TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(userName: CharSequence?, start: Int, before: Int, count: Int) {
        credentialsInputListener.onUsernameEdit(userName.toString())
    }
}