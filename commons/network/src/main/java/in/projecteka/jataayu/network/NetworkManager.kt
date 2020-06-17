package `in`.projecteka.jataayu.network

import `in`.projecteka.jataayu.network.interceptor.HostSelectionInterceptor
import `in`.projecteka.jataayu.network.interceptor.NetworkConnectionInterceptor
import `in`.projecteka.jataayu.network.interceptor.RequestInterceptor
import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.CONNECT_TIMEOUT
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.MOCK_WEB_SERVER_TEST_URL
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.READ_TIMEOUT
import `in`.projecteka.jataayu.util.constant.NetworkConstants.Companion.WRITE_TIMEOUT
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.sharedPref.getBaseUrl
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class NetworkManager private constructor() {

    companion object {
        lateinit var context: Context
            private set
        fun createNetworkClient(
            context: Context,
            credentialsRepository: CredentialsRepository,
            debug: Boolean = false
        ): Retrofit {
            NetworkManager.context = context
            return NetworkManager().createNetworkClient(credentialsRepository, debug)
        }

        fun hasInternetConnection(): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network: Network? = connectivityManager.activeNetwork
            val capabilities = connectivityManager
                .getNetworkCapabilities(network)
            return (capabilities != null
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        }
    }

    private val isTestingMode: Boolean
        get() = context.javaClass.simpleName != "JataayuApp"
    private val gson: Gson
        get() = GsonBuilder().create()


    private fun createNetworkClient(
        credentialsRepository: CredentialsRepository,
        debug: Boolean = false
    ): Retrofit {

        return buildRetrofitClient(
            getBaseUrl(),
            httpClient(debug, context, credentialsRepository)
        )
    }


    private fun getBaseUrl(): String {
        return if (isTestingMode) MOCK_WEB_SERVER_TEST_URL else context.getBaseUrl()
    }

    private fun httpClient(
        debug: Boolean,
        context: Context,
        credentialsRepository: CredentialsRepository
    ): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
        // Install the all-trusting trust manager
        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.sslSocketFactory(
            sslSocketFactory,
            trustAllCerts[0] as X509TrustManager
        )
        clientBuilder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        clientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        clientBuilder.addInterceptor(RequestInterceptor(credentialsRepository))

        if (debug && !isTestingMode) {
            addRequestResponseLogger(httpLoggingInterceptor, clientBuilder)
            addBaseUrlChanger(clientBuilder, context)
        }
        addInvalidSessionRedirectInterceptor(context, context.getBaseUrl(), clientBuilder)
        addResponseCacheInterceptor(clientBuilder, context)
        addNoInternetConnectionInterceptor(context, clientBuilder)
        return clientBuilder.build()
    }

    private fun addBaseUrlChanger(clientBuilder: OkHttpClient.Builder, context: Context) {
        clientBuilder.addInterceptor(HostSelectionInterceptor(context))
    }

    private fun addRequestResponseLogger(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        clientBuilder: OkHttpClient.Builder
    ) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
    }

    private fun addInvalidSessionRedirectInterceptor(
        context: Context,
        baseUrl: String,
        clientBuilder: OkHttpClient.Builder
    ) {
        clientBuilder.addInterceptor(UnauthorisedUserRedirectInterceptor(context, baseUrl))
    }

    private fun addNoInternetConnectionInterceptor(
        context: Context,
        clientBuilder: OkHttpClient.Builder
    ) {
        clientBuilder.addInterceptor(NetworkConnectionInterceptor(context))
    }

    private fun addResponseCacheInterceptor(
        clientBuilder: OkHttpClient.Builder,
        context: Context
    ) {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)
        clientBuilder.cache(myCache)
    }

    private fun buildRetrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}



