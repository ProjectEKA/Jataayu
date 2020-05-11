package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RequestedConsentListViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var preferenceRepo: PreferenceRepository

    @Mock
    private lateinit var credentialsRepo: CredentialsRepository

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    private lateinit var consentViewModel: RequestedListViewModel
    @Mock
    private lateinit var consentsFetchObserver: Observer<PayloadResource<ConsentsListResponse>>

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = RequestedListViewModel(repository)

        consentsListResponse = Gson()
            .fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)

    }

    @After
    fun tearDown() {
        consentViewModel.consentListResponse.removeObserver(consentsFetchObserver)
    }

    @Test
    fun `should Populate Filter Items For Requested Consents`() {
        `when`(resources.getString(R.string.status_active_requested_consents)).thenReturn("Active requested consents")
        `when`(resources.getString(R.string.status_expired_requested_consents)).thenReturn("Expired requested consents")
        `when`(resources.getString(R.string.status_denied_consent_requests)).thenReturn("Denied consent requests")
        `when`(resources.getString(R.string.status_all_requested_consents)).thenReturn("All requested consents")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources)

        assertEquals(dummyRequestedFilterList(), populatedFilterItems)
    }

    private fun dummyRequestedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("Active requested consents")
        list.add("Expired requested consents")
        list.add("Denied consent requests")
        list.add("All requested consents")
        return list
    }

    @Test
    fun `should Fetch Consents`() {

        `when`(repository.getConsents(10, 0, null)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(consentsListResponse))
            }

        consentViewModel.consentListResponse.observeForever(consentsFetchObserver)

        consentViewModel.getConsents(null, offset = 0)

        verify(consentsFetchObserver, times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, times(1)).onChanged(Success(consentsListResponse))
    }

    @Test
    fun `should Fetch only active consents`() {

        consentViewModel.currentStatus.value = RequestStatus.REQUESTED
        val activeConsents = filterConsents(consentViewModel.currentStatus.value!!)
        val expectedResponse = ConsentsListResponse(activeConsents, activeConsents.size, 0)
        val filters = consentViewModel.currentStatus.value?.let { "status==${it.name}" }
        `when`(repository.getConsents(10, 0, filters)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(expectedResponse))
            }

        consentViewModel.consentListResponse.observeForever(consentsFetchObserver)

        consentViewModel.getConsents(consentViewModel.currentStatus.value, offset = 0)

        verify(repository).getConsents(10, 0, filters)
        verify(call).enqueue(any())
        assertEquals(expectedResponse.requests, activeConsents)

    }

    @Test
    fun `should Fetch only expired consents`() {

        consentViewModel.currentStatus.value = RequestStatus.EXPIRED
        val activeConsents = filterConsents(consentViewModel.currentStatus.value!!)
        val expectedResponse = ConsentsListResponse(activeConsents, activeConsents.size, 0)
        val filters = consentViewModel.currentStatus.value?.let { "status==${it.name}" }
        `when`(repository.getConsents(10, 0, filters)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(expectedResponse))
            }

        consentViewModel.consentListResponse.observeForever(consentsFetchObserver)

        consentViewModel.getConsents(consentViewModel.currentStatus.value, offset = 0)

        verify(repository).getConsents(10, 0, filters)
        verify(call).enqueue(any())
        assertEquals(expectedResponse.requests, activeConsents)

    }

    @Test
    fun `should Fetch only denied consents`() {

        consentViewModel.currentStatus.value = RequestStatus.DENIED
        val activeConsents = filterConsents(consentViewModel.currentStatus.value!!)
        val expectedResponse = ConsentsListResponse(activeConsents, activeConsents.size, 0)
        val filters = consentViewModel.currentStatus.value?.let { "status==${it.name}" }
        `when`(repository.getConsents(10, 0, filters)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(expectedResponse))
            }

        consentViewModel.consentListResponse.observeForever(consentsFetchObserver)

        consentViewModel.getConsents(consentViewModel.currentStatus.value, offset = 0)

        verify(repository).getConsents(10, 0, filters)
        verify(call).enqueue(any())
        assertEquals(expectedResponse.requests, activeConsents)

    }



    private fun dummyRequestedConsentsList(): List<Consent>? {
        return getData("requested_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))

    private fun filterConsents(status: RequestStatus) : List<Consent> {
        return consentsListResponse.requests.asSequence().filter { it.status == status }.toList()
    }

}