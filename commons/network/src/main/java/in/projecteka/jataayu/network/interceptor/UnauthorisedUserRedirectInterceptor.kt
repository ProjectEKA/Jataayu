package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.sharedPref.resetCredentials
import android.content.Context
import android.content.Intent
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

class UnauthorisedUserRedirectInterceptor(private val context: Context, private val baseUrl: String) : Interceptor {

    companion object {
        private const val REDIRECT_ACTIVITY_ACTION = "in.projecteka.jataayu.home"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        return when (response.request.url.toString()) {
            "${baseUrl}users/verify",
            "${baseUrl}users/permit",
            "${baseUrl}sessions",
            "${baseUrl}patients/verify-pin" -> {
                response
            }
            else -> {
                if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == HttpURLConnection.HTTP_FORBIDDEN) {
                    context.resetCredentials()
                    context.showLongToast("Session expired, redirecting to Login...")
                    val intent = Intent().apply {
                        action = REDIRECT_ACTIVITY_ACTION
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    context.startActivity(intent)
                }
                response
            }
        }
    }
}
