package `in`.projecteka.jataayu.util.constant

import `in`.projecteka.jataayu.util.BuildConfig

class NetworkConstants {
    companion object {
        var PROD_URL : String = BuildConfig.PROD_URL
        const val TEST_URL : String = "http://10.0.2.2:8000/"   
            const val MOCKOON_URL : String = "http://10.0.2.2:3000/"
        const val MOCK_WEB_SERVER_TEST_URL : String = "http://localhost:8080"
        const val CONNECT_TIMEOUT = 15L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 30L
    }
}