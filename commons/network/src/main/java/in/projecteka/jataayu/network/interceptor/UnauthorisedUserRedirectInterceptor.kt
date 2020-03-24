package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.util.event.UserUnauthorizedRedirectEvent
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class UnauthorisedUserRedirectInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        return when (response.request.url.encodedPath) {
            "/users/verify",
            "/users/permit",
            "/sessions",
            "/patients/verify-pin" -> {
                response
            }
            else -> {
                if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED || response.code == HttpURLConnection.HTTP_FORBIDDEN) {
                    EventBus.getDefault().post(UserUnauthorizedRedirectEvent.REDIRECT)
                }
                response
            }
        }
    }
}
