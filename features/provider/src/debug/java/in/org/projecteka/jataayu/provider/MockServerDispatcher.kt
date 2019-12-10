package `in`.org.projecteka.jataayu.provider

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.apache.commons.io.FileUtils.readFileToString
import java.io.File


class MockServerDispatcher {

    /**
     * Return ok response from mock server
     */
    inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {

            val successResponse = MockResponse().setResponseCode(200)
            return when (request.path) {
                "/providers?name=Health" -> successResponse.setBody(readFileToString(
                    File("src/sharedTest/resources/health_insurance_providers.json")))
                "/providers?name=SomeInvalidName" -> successResponse.setBody(emptyProvidersResponse)
                else -> MockResponse().setResponseCode(404)
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
