package `in`.projecteka.jataayu.util.repository

import `in`.projecteka.jataayu.util.extension.toUtc
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.milliseconds

interface CredentialsRepository {
    var accessToken: String?
    var consentTemporaryToken: String?
    var refreshToken: String?
    var refreshTokenExpiresIn: Long
    var accessTokenExpiresIn: Long

    fun isRefreshTokeExpired(): Boolean
    fun isAccessTokeExpired(): Boolean

    fun reset()
}

open class CredentialsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    CredentialsRepository {

    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val CONSENT_TEMP_TOKEN = "CONSENT_TEMP_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val REFRESH_TOKEN_EXPIRE_IN = "REFRESH_TOKEN_EXPIRE_IN"
        private const val ACCESS_TOKEN_EXPIRE_IN = "ACCESS_TOKEN_EXPIRE_IN"
    }


    override var accessToken: String?
        set(value) = sharedPreferences.edit { putString(ACCESS_TOKEN, value) }
        get() = sharedPreferences.getString(ACCESS_TOKEN, null)

    override var consentTemporaryToken: String?
        set(value) = sharedPreferences.edit { putString(CONSENT_TEMP_TOKEN, value) }
        get() = sharedPreferences.getString(CONSENT_TEMP_TOKEN, null)

    override var refreshToken: String?
        set(value) = sharedPreferences.edit { putString(REFRESH_TOKEN, value) }
        get() = sharedPreferences.getString(REFRESH_TOKEN, null)
    override var refreshTokenExpiresIn: Long
        get() = sharedPreferences.getLong(REFRESH_TOKEN_EXPIRE_IN, 0)
        set(value) {
            val newValue = TimeUnit.MILLISECONDS.toSeconds(Date().time) + value
            sharedPreferences.edit { putLong(REFRESH_TOKEN_EXPIRE_IN, newValue) }
        }
    override var accessTokenExpiresIn: Long
        get() = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRE_IN, 0)
        set(value) {
            val newValue = TimeUnit.MILLISECONDS.toSeconds(Date().time) + value
            sharedPreferences.edit { putLong(ACCESS_TOKEN_EXPIRE_IN, newValue) }
        }

    override fun isRefreshTokeExpired(): Boolean {
        return if(refreshTokenExpiresIn != 0L) {
            refreshTokenExpiresIn < TimeUnit.MILLISECONDS.toSeconds(Date().time)
        } else {
            false
        }
    }

    override fun isAccessTokeExpired(): Boolean {
        return if(accessTokenExpiresIn != 0L) {
            accessTokenExpiresIn < TimeUnit.MILLISECONDS.toSeconds(Date().time)
        } else {
            false
        }
    }

    private fun getSecond(): Long {
        val calendarToday = Calendar.getInstance()
        calendarToday.timeZone = TimeZone.getTimeZone("UTC")
        calendarToday.timeInMillis = Date().time
        val seconds = TimeUnit.MILLISECONDS.toSeconds(calendarToday.timeInMillis)
        return seconds
    }

    override fun reset() {
        accessToken = null
        consentTemporaryToken = null
        refreshToken = null
        refreshTokenExpiresIn=0
        accessTokenExpiresIn=0
    }
}