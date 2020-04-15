package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.util.sharedPref.getAuthToken
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val modifiedRequest = original.newBuilder()
            .header("Content-Type", "application/json")
            .method(original.method, original.body)
        if (original.header("Authorization") == null) {
            modifiedRequest.header("Authorization", context.getAuthToken())
        }
        return chain.proceed(modifiedRequest.build())
    }
}
