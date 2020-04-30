package `in`.projecteka.jataayu.user.account.listener

import android.text.Editable
import android.text.TextWatcher

class AyushmanIdChangeWatcher(private val credentialsInputListener: CredentialsInputListener): TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(ayushmanId: CharSequence?, start: Int, before: Int, count: Int) {
        ayushmanId.toString().capitalize()
        credentialsInputListener.onAyushmanIdEdit(ayushmanId.toString().capitalize())
    }
}