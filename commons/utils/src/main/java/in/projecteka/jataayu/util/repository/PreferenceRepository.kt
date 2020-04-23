package `in`.projecteka.jataayu.util.repository

import android.content.SharedPreferences
import androidx.core.content.edit

interface PreferenceRepository {

    var name: String?

    var mobileIdentifier: String?

    var pinCreated: Boolean

    var isUserLoggedIn: Boolean

    var isUserRegistered: Boolean

    var isUserAccountCreated: Boolean

    var hasProviders: Boolean

    fun resetCredentials()
}

class PreferenceRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    PreferenceRepository {

    companion object {
        private const val PIN_CREATED = "PIN_CREATED"
        private const val LOGGED_IN = "logged_in"
        private const val REGISTERED = "registered"
        private const val ACCOUNT_CREATED = "account_created"
        private const val PROVIDER_ADDED = "provider_added"
        private const val NAME = "NAME"
        private const val MOBILE_NUMBER = "MOBILE_NUMBER"
    }

    override var pinCreated: Boolean
        set(value) = sharedPreferences.edit { putBoolean(PIN_CREATED, value) }
        get() = sharedPreferences.getBoolean(PIN_CREATED, false)

    override var name: String?
        get() = sharedPreferences.getString(NAME, null)
        set(value) = sharedPreferences.edit { putString(NAME, value) }

    override var isUserLoggedIn: Boolean
        set(value) = sharedPreferences.edit { putBoolean(LOGGED_IN, value) }
        get() = sharedPreferences.getBoolean(LOGGED_IN, false)

    override var isUserRegistered: Boolean
        set(value) = sharedPreferences.edit { putBoolean(REGISTERED, value) }
        get() = sharedPreferences.getBoolean(REGISTERED, false)

    override var isUserAccountCreated: Boolean
        set(value) = sharedPreferences.edit { putBoolean(ACCOUNT_CREATED, value) }
        get() = sharedPreferences.getBoolean(ACCOUNT_CREATED, false)

    override var hasProviders: Boolean
        set(value) = sharedPreferences.edit { putBoolean(PROVIDER_ADDED, value) }
        get() = sharedPreferences.getBoolean(PROVIDER_ADDED, false)

    override var mobileIdentifier: String?
        get() = sharedPreferences.getString(MOBILE_NUMBER, null)
        set(value) = sharedPreferences.edit { putString(MOBILE_NUMBER, value) }

    override fun resetCredentials() {
        pinCreated = false
        name = null
        isUserLoggedIn = false
        isUserRegistered = false
        isUserAccountCreated = false
        hasProviders = false
        mobileIdentifier = null
    }
}