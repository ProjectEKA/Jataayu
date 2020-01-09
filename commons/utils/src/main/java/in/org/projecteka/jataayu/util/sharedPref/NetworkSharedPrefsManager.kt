package `in`.org.projecteka.jataayu.util.sharedPref

import `in`.org.projecteka.jataayu.util.constant.NetworkConstants.Companion.PROD_URL
import android.content.Context

class NetworkSharedPrefsManager {

    companion object {
        private const val NETWORK_PREF = "NETWORK_PREF_FILE"
        private const val ENVIRONMENT = "ENVIRONMENT"
        private const val NETWORK_HOST = "NETWORK_PREF_HOST"

        fun setNetworkPref(context: Context, environmentIndex: Int, endpoint: String) {
            val sharedPreferences = context.getSharedPreferences(NETWORK_PREF, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(ENVIRONMENT, environmentIndex)
            editor.putString(NETWORK_HOST, endpoint)
            editor.apply()
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