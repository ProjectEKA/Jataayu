package `in`.projecteka.jataayu.util.repository

import android.content.SharedPreferences
import androidx.core.content.edit

interface CredentialsRepository {
    var accessToken: String?
    var consentTemporaryToken: String?

    fun reset()
}

class CredentialsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    CredentialsRepository {

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val CONSENT_TEMP_TOKEN = "CONSENT_TEMP_TOKEN"
    }


    override var accessToken: String?
        set(value) = sharedPreferences.edit { putString(AUTH_TOKEN, value) }
        get() = sharedPreferences.getString(AUTH_TOKEN, null)

    override var consentTemporaryToken: String?
        set(value) = sharedPreferences.edit { putString(CONSENT_TEMP_TOKEN, value) }
        get() = sharedPreferences.getString(CONSENT_TEMP_TOKEN, null)

    override fun reset() {
        accessToken = null
        consentTemporaryToken = null
    }
}