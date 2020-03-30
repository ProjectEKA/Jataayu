package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.util.event.UserUnauthorizedRedirectEvent
import `in`.projecteka.jataayu.util.sharedPref.getBaseUrl
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import org.koin.core.context.GlobalContext.get
import java.net.HttpURLConnection

class UnauthorisedUserRedirectInterceptor(private val baseUrl: String, private val eventBusInstance: EventBus) : Interceptor {

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
                    eventBusInstance.post(UserUnauthorizedRedirectEvent.REDIRECT)
                }
                response
            }
        }
    }
}
