package `in`.projecteka.jataayu.testUtil

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MockServerDispatcher {

    /**
     * Return ok response from mock server
     */
    inner class RequestDispatcher(private val context: Context) : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {

            Timber.d("url is ${request.path}")
            val mockResponse = MockResponse().setResponseCode(200)
            return when {
                request.path == "/providers/10000005" -> mockResponse.setBody(
                    AssetReaderUtil.asset(context, "provider_info_by_id_response.json"))
                    .setBodyDelay(0, TimeUnit.MILLISECONDS)

                request.path!!.startsWith("/providers?name=H") -> mockResponse.setBody(
                    AssetReaderUtil.asset(context, "health_insurance_providers.json")
                )

                request.path!!.startsWith("/requests") -> mockResponse.setBody(
                    AssetReaderUtil.asset(context, "consent_list_response.json")
                ).setBodyDelay(0, TimeUnit.MILLISECONDS)

                request.path!!.startsWith("/consent-requests") -> mockResponse.setBody(
                    AssetReaderUtil.asset(context, "consent-requests.json")
                ).setBodyDelay(0, TimeUnit.MILLISECONDS)

                request.path!!.startsWith("/patients/links") -> mockResponse.setBody(
                    AssetReaderUtil.asset(context, "linked_accounts.json"))
                    .setBodyDelay(0, TimeUnit.MILLISECONDS)

                else -> mockResponse.setResponseCode(404)
            }
        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {

            return MockResponse().setResponseCode(400)

        }
    }
}

const val emptyProvidersResponse = "[]"
