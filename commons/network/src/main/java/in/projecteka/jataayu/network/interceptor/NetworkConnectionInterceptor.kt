package `in`.projecteka.jataayu.network.interceptor

import `in`.projecteka.jataayu.network.NetworkManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException

class NetworkConnectionInterceptor() : Interceptor, KoinComponent {

    private val networkManager: NetworkManager by inject()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkManager.hasInternetConnection()) {
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