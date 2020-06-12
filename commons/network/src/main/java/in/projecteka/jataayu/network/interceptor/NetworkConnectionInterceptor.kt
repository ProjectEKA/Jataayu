package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.network.hasInternetConnection
import `in`.projecteka.jataayu.util.startNoInternetConnectionScreen
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hasInternetConnection(context)) {
            startNoInternetConnectionScreen(context) {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

class NoConnectivityException : IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "No Internet Connection"
}