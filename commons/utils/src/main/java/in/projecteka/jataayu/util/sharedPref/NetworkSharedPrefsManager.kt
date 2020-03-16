package `in`.projecteka.jataayu.util.sharedPref

import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import android.content.Context
import androidx.core.content.edit

private const val NETWORK_PREF = "NETWORK_PREF_FILE"
private const val ENVIRONMENT = "ENVIRONMENT"
private const val NETWORK_HOST = "NETWORK_PREF_HOST"
private const val AUTH_TOKEN = "AUTH_TOKEN"
private const val CONSENT_TEMP_TOKEN = "CONSENT_TEMP_TOKEN"
private const val CONSENT_CREATION_PIN_API_INTEGRATION = "CONSENT_CREATION_PIN_API_INTEGRATION"
private const val HAS_TRANSACTION_PIN = "HAS_TRANSACTION_PIN"

fun Context.setNetworkPref(environmentIndex: Int, endpoint: String) {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt(ENVIRONMENT, environmentIndex)
        putString(NETWORK_HOST, endpoint)
    }
}

fun Context.setAuthToken(authToken: String) {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    sharedPreferences.edit { putString(AUTH_TOKEN, authToken) }
}

fun Context.getAuthToken(): String {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    return sharedPreferences.getString(AUTH_TOKEN, "") ?: ""
}

fun Context.getBaseUrl(): String {
    return getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
        .getString(NETWORK_HOST, PROD_URL) ?: PROD_URL
}

fun Context.getEndpointIndex(): Int {
    return getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE).getInt(ENVIRONMENT, 0)
}

fun Context.setConsentPinCreationAPIintegrationStatus(status: Boolean){
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    sharedPreferences.edit { putBoolean(CONSENT_CREATION_PIN_API_INTEGRATION, status) }
}

fun Context.getConsentPinCreationAPIintegrationStatus(): Boolean {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(CONSENT_CREATION_PIN_API_INTEGRATION, false) ?: false
}

fun Context.setConsentTempToken(tempToken: String) {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    sharedPreferences.edit { putString(CONSENT_TEMP_TOKEN, tempToken) }
}

fun Context.getConsentTempToken(): String {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    return sharedPreferences.getString(CONSENT_TEMP_TOKEN, "") ?: ""
}
fun Context.setConsentPinStatus(status: Boolean) {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    sharedPreferences.edit { putBoolean(HAS_TRANSACTION_PIN, status) }
}

fun Context.getConsentPinStatus(): Boolean {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(HAS_TRANSACTION_PIN, false) ?: false
}
