package `in`.projecteka.jataayu.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response


class ResponseCacheInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val cacheControl = request.header("Cache-Control")
        if (cacheControl.isNullOrEmpty()) {
            return  chain.proceed(request)
        } else {
            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 5).build()
            return chain.proceed(request)
        }
    }
}