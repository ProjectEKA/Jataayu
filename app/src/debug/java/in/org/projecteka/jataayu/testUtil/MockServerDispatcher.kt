package `in`.org.projecteka.jataayu.testUtil

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit


class MockServerDispatcher {

    /**
     * Return ok response from mock server
     */
    inner class RequestDispatcher(private val context: Context) : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {

            val successResponse = MockResponse().setResponseCode(200)
            return when {
                request.path!!.startsWith("/providers?name=H") -> successResponse.setBody(
                    AssetReaderUtil.asset(context, "health_insurance_providers.json")
                )

                request.path!!.startsWith("/requests") -> successResponse.setBody(
                    AssetReaderUtil.asset(context, "consent_list_response.json")
                ).setBodyDelay(300, TimeUnit.MILLISECONDS)

                request.path!!.startsWith("/consent-requests") -> successResponse.setBody(
                    AssetReaderUtil.asset(context, "consent-requests.json")
                ).setBodyDelay(300, TimeUnit.MILLISECONDS)

                request.path!!.startsWith("/patients/links") -> successResponse.setBody(
                    AssetReaderUtil.asset(context, "linked_accounts.json"))
                    .setBodyDelay(300, TimeUnit.MILLISECONDS)

                else -> successResponse.setBody(emptyProvidersResponse)
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
