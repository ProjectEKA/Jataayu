package `in`.projecteka.jataayu.network

import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import android.content.Context
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


@RunWith(MockitoJUnitRunner::class)
class RedirectInterceptorTest {


    lateinit var mockWebServer: MockWebServer
    lateinit var okHttpClient: OkHttpClient
    lateinit var retrofit: Retrofit
    lateinit var testApi: TestAPI

    @Mock
    lateinit var context: Context

    lateinit var redirectInterceptor: UnauthorisedUserRedirectInterceptor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        redirectInterceptor = UnauthorisedUserRedirectInterceptor(context, mockWebServer.url("/").toString())
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(redirectInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        testApi = retrofit.create(TestAPI::class.java)

        doNothing().`when`(context).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a response with 401 response code`() {
        val response = MockResponse().apply {
            setResponseCode(401)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.test().execute()

        verify(context, times(1)).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a response with 403 response code`() {
        val response = MockResponse().apply {
            setResponseCode(403)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.test().execute()

        verify(context, times(1)).startActivity(ArgumentMatchers.any())

    }

    @Test
    fun `test for a response with 5XX response code`() {
        val response = MockResponse().apply {
            setResponseCode(503)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.test().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a response with 2XX response code`() {
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.test().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a request having path patient users|verify`() {
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.url("/users/verify")
        mockWebServer.enqueue(response)

        testApi.test().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a request having path patient users|permit`() {
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.url("/users/permit")
        mockWebServer.enqueue(response)

        testApi.test().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a request having path |session`() {
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.getSession().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a request having path patients|verify-pin and returning 401 status code`() {
        val response = MockResponse().apply {
            setResponseCode(401)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.verifyPatient().execute()
        verify(context, never()).startActivity(ArgumentMatchers.any())


    }

    @Test
    fun `test for a request having path users|permit but returning 200 status code`() {
        val response = MockResponse().apply {
            setResponseCode(200)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.permitUser().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for a request having path users|permit but returning 403 status code`() {
        val response = MockResponse().apply {
            setResponseCode(403)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.permitUser().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    @Test
    fun `test for login having path session but returning 401 status code`() {
        val response = MockResponse().apply {
            setResponseCode(401)
            setBody("{\"one\":\"two\",\"key\":\"value\"}")
        }
        mockWebServer.enqueue(response)

        testApi.getSession().execute()

        verify(context, never()).startActivity(ArgumentMatchers.any())
    }

    interface TestAPI {

        @GET("/")
        fun test(): Call<JsonObject>

        @GET("patients/verify-pin")
        fun verifyPatient(): Call<JsonObject>

        @GET("users/permit")
        fun permitUser(): Call<JsonObject>

        @GET("sessions")
        fun getSession(): Call<JsonObject>

    }

}