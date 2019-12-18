package `in`.org.projecteka.jataayu.provider

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest


class MockServerDispatcher {

    /**
     * Return ok response from mock server
     */
    inner class RequestDispatcher(val context : Context) : Dispatcher() {
        override fun dispatch(request : RecordedRequest) : MockResponse {

            val successResponse = MockResponse().setResponseCode(200)
            return when {
                request.path!!.startsWith("/providers/?name=H") -> successResponse.setBody(
                    AssetReaderUtil.asset(context, "health_insurance_providers.json"))
                else -> successResponse.setBody(emptyProvidersResponse)
            }
        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request : RecordedRequest) : MockResponse {

            return MockResponse().setResponseCode(400)

        }
    }
}

const val emptyProvidersResponse = "[]"
