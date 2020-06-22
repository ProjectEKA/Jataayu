package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.util.sharedPref.getBaseUrl
import android.content.Context
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HostSelectionInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = context.getBaseUrl()
        val newRequest = url.toHttpUrlOrNull()?.let {
            val newUrl = chain.request().url.newBuilder()
                    .scheme(it.scheme)
                    .host(it.toUrl().toURI().host)
                    .port(it.port)
                    .build()

            return@let chain.request().newBuilder()
                    .url(newUrl)
                    .build()
        }

        return chain.proceed(newRequest!!)
    }
}