package `in`.projecteka.jataayu.network.interceptor

import android.content.Context
import android.content.Intent
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

class UnauthorisedUserRedirectInterceptor(private val context: Context, private val baseUrl: String) : Interceptor {

    companion object {
        const val REDIRECT_ACTIVITY_ACTION = "in.projecteka.jataayu.home"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val requestURL = response.request.url

        return when (requestURL.toString()) {
            "${baseUrl}users/verify",
            "${baseUrl}users/permit",
            "${baseUrl}patients/verify-pin",
            "${baseUrl}patients/link/${requestURL.pathSegments.last()}",
            "${baseUrl}sessions" -> {
                response
            }
            else -> {
                if (isSessionInvalid(response.code)) {
                    context.startActivity(getRedirectIntent())
                }
                response
            }
        }
    }

    private fun isSessionInvalid(statusCode: Int) = statusCode == HttpURLConnection.HTTP_UNAUTHORIZED || statusCode == HttpURLConnection
        .HTTP_FORBIDDEN

    private fun getRedirectIntent() = Intent().apply {
        action = REDIRECT_ACTIVITY_ACTION
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}
