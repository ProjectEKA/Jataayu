package `in`.org.projecteka.jataayu.network.interceptor

import `in`.org.projecteka.jataayu.network.BuildConfig
import `in`.org.projecteka.jataayu.util.sharedPref.NetworkSharedPrefsManager
import android.content.Context
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HostSelectionInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = NetworkSharedPrefsManager.getBaseUrl(context = context)!!
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