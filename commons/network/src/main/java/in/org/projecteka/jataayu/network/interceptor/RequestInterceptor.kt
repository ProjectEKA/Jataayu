package `in`.org.projecteka.jataayu.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val authToken: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val modifiedRequest = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", authToken!!)
            .method(original.method, original.body)
            .build()

        return chain.proceed(modifiedRequest)
    }
}
