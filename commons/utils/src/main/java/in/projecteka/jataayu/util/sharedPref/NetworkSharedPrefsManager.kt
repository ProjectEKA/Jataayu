package `in`.projecteka.jataayu.util.sharedPref

import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import android.content.Context
import androidx.core.content.edit

const val NETWORK_PREF = "NETWORK_PREF_FILE"
private const val ENVIRONMENT = "ENVIRONMENT"
private const val NETWORK_HOST = "NETWORK_PREF_HOST"

fun Context.setNetworkPref(environmentIndex: Int, endpoint: String) {
    val sharedPreferences = getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt(ENVIRONMENT, environmentIndex)
        putString(NETWORK_HOST, endpoint)
    }
}

fun Context.getBaseUrl(): String {
    return getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
        .getString(NETWORK_HOST, PROD_URL) ?: PROD_URL
}

fun Context.getEndpointIndex(): Int {
    return getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE).getInt(ENVIRONMENT, 0)
}