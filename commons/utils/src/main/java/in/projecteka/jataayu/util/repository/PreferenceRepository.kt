package `in`.projecteka.jataayu.util.repository

import android.content.SharedPreferences
import androidx.core.content.edit

interface PreferenceRepository {

    companion object {
        const val VERIFIED_IDENTIFIER_TYPE_MOBILE = "MOBILE"
        const val MOBILE_NUMBER_DELIMITER = "-"
        const val GENDER_MALE = "M"
        const val GENDER_FEMALE = "F"
        const val GENDER_OTHERS = "O"
        const val TYPE_AYUSHMAN_BHARAT_ID = "ABPMJAYID"
        const val TYPE_PAN = "PAN"
    }

    var name: String?

    var countryCode: String?

    var mobileIdentifier: String?

    var pinCreated: Boolean

    var isUserLoggedIn: Boolean

    var isUserRegistered: Boolean

    var isUserAccountCreated: Boolean

    var hasProviders: Boolean

    var consentManagerId: String?

    var ayushmanBharatId: String?

    var pan: String?

    var gender: String?

    var yearOfBirth: Int

    var loginMode : String?

    fun resetPreferences()
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
        private const val COUNTRY_CODE = "COUNTRY_CODE"
        private const val MOBILE_NUMBER = "MOBILE_NUMBER"
        private const val CONSENT_MANAGER_ID = "CONSENT_MANAGER_ID"
        private const val AYUSHMAN_BHARAT_ID = "AYUSHMAN_BHARAT_ID"
        private const val PAN = "PAN"
        private const val GENDER = "GENDER"
        private const val YEAR_OF_BIRTH = "YEAR_OF_BIRTH"
        private const val LOGIN_MODE = "LOGIN_MODE"
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

    override var countryCode: String?
        get() = sharedPreferences.getString(COUNTRY_CODE, null)
        set(value) = sharedPreferences.edit { putString(COUNTRY_CODE, value) }

    override var mobileIdentifier: String?
        get() = sharedPreferences.getString(MOBILE_NUMBER, null)
        set(value) = sharedPreferences.edit { putString(MOBILE_NUMBER, value) }

    override var consentManagerId: String?
        get() = sharedPreferences.getString(CONSENT_MANAGER_ID, null)
        set(value) = sharedPreferences.edit { putString(CONSENT_MANAGER_ID, value) }

    override var ayushmanBharatId: String?
        get() = sharedPreferences.getString(AYUSHMAN_BHARAT_ID, null)
        set(value) = sharedPreferences.edit { putString(AYUSHMAN_BHARAT_ID, value) }

    override var pan: String?
        get() = sharedPreferences.getString(PAN, null)
        set(value) = sharedPreferences.edit { putString(PAN, value) }

    override var gender: String?
        get() = sharedPreferences.getString(GENDER, null)
        set(value) = sharedPreferences.edit { putString(GENDER, value) }

    override var yearOfBirth: Int
        get() = sharedPreferences.getInt(YEAR_OF_BIRTH, 0)
        set(value) = sharedPreferences.edit { putInt(YEAR_OF_BIRTH, value) }

    override var loginMode: String?
        get() = sharedPreferences.getString(LOGIN_MODE, null)
        set(value) = sharedPreferences.edit { putString(LOGIN_MODE, value) }

    override fun resetPreferences() {
        pinCreated = false
        name = null
        isUserLoggedIn = false
        isUserRegistered = false
        isUserAccountCreated = false
        hasProviders = false
        countryCode = null
        mobileIdentifier = null
        consentManagerId = null
        gender = null
        yearOfBirth = 0
        ayushmanBharatId = null
        pan = null
        loginMode=null
    }
}