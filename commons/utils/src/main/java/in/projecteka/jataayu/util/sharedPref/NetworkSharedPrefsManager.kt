package `in`.projecteka.jataayu.util.sharedPref

import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import android.content.Context
import androidx.core.content.edit

class NetworkSharedPrefsManager {

    companion object {
        private const val NETWORK_PREF = "NETWORK_PREF_FILE"
        private const val ENVIRONMENT = "ENVIRONMENT"
        private const val NETWORK_HOST = "NETWORK_PREF_HOST"
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val DEFAULT_EXPIRY = -1
        fun setNetworkPref(context: Context, environmentIndex: Int, endpoint: String) {
            val sharedPreferences = context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putInt(ENVIRONMENT, environmentIndex)
                putString(NETWORK_HOST, endpoint)
            }
        }

        fun setAuthToken(context: Context, authToken: String) {
            val sharedPreferences = context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
            sharedPreferences.edit { putString(AUTH_TOKEN, authToken) }
        }

        fun getAuthToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
            return sharedPreferences.getString(AUTH_TOKEN, "")
        }

        fun getBaseUrl(context: Context): String? {
            return context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
                .getString(NETWORK_HOST, PROD_URL)
        }

        fun getEndpointIndex(context: Context): Int {
            return context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE).getInt(
                ENVIRONMENT, 0)
        }

    }

}