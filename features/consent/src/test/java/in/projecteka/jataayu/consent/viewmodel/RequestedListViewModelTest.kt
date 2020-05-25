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
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    private lateinit var requestedListViewModel: RequestedListViewModel
    @Mock
    private lateinit var consentsFetchObserver: Observer<PayloadResource<ConsentsListResponse>>

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        requestedListViewModel = RequestedListViewModel(repository)
        consentsListResponse = Gson()
            .fromJson(
                TestUtils.readFile("consent_list_response.json"),
                ConsentsListResponse::class.java
            )
        requestedListViewModel.consentListResponse.observeForever(consentsFetchObserver)

    }

    @After
    fun tearDown() {
        requestedListViewModel.consentListResponse.removeObserver(consentsFetchObserver)
    }

    @Test
    fun `should Populate Filter Items For Requested Consents`() {
        `when`(resources.getString(R.string.status_active_requested_consents)).thenReturn("Active requested consents")
        `when`(resources.getString(R.string.status_expired_requested_consents)).thenReturn("Expired requested consents")
        `when`(resources.getString(R.string.status_denied_consent_requests)).thenReturn("Denied consent requests")
        `when`(resources.getString(R.string.status_all_requested_consents)).thenReturn("All requested consents")
        val populatedFilterItems = requestedListViewModel.populateFilterItems(resources)

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
    fun `should Fetch all consents`() {

        requestedListViewModel.updateFilterSelectedItem(3)
        setupConsentAPI()
        requestedListViewModel.getConsents(offset = 0)

        verify(consentsFetchObserver, times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, times(1)).onChanged(Success(consentsListResponse))
    }

    @Test
    fun `should Fetch only active consents`() {

        requestedListViewModel.updateFilterSelectedItem(0)
        setupConsentAPI()
        requestedListViewModel.getConsents(offset = 0)

        verify(repository).getConsents(10, 0, RequestStatus.REQUESTED.name)
        verify(call).enqueue(any())

        verify(consentsFetchObserver, times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, times(1)).onChanged(Success(consentsListResponse))
    }

    @Test
    fun `should Fetch only expired consents`() {

        requestedListViewModel.updateFilterSelectedItem(1)
        setupConsentAPI()
        requestedListViewModel.getConsents(offset = 0)

        verify(repository).getConsents(10, 0, RequestStatus.EXPIRED.name)
        verify(call).enqueue(any())

        verify(consentsFetchObserver, times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, times(1)).onChanged(Success(consentsListResponse))

    }

    @Test
    fun `should Fetch only denied consents`() {

        requestedListViewModel.updateFilterSelectedItem(2)
        setupConsentAPI()
        requestedListViewModel.getConsents(offset = 0)

        verify(repository).getConsents(10, 0, RequestStatus.DENIED.name)
        verify(call).enqueue(any())

        verify(consentsFetchObserver, times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, times(1)).onChanged(Success(consentsListResponse))

    }

    @Test
    fun `should change current status to requested when filter is selected to 1st item`() {
        requestedListViewModel.updateFilterSelectedItem(0)
        assertEquals(RequestStatus.REQUESTED, requestedListViewModel.currentStatus.value)
    }

    @Test
    fun `should change current status to expired when filter is selected to 2nd item`() {
        requestedListViewModel.updateFilterSelectedItem(1)
        assertEquals(RequestStatus.EXPIRED, requestedListViewModel.currentStatus.value)
    }

    @Test
    fun `should change current status to denied when filter is selected to 3rd item`() {
        requestedListViewModel.updateFilterSelectedItem(2)
        assertEquals(RequestStatus.DENIED, requestedListViewModel.currentStatus.value)
    }

    @Test
    fun `should change current status to all when filter is selected to 4th item`() {
        requestedListViewModel.updateFilterSelectedItem(3)
        assertEquals(RequestStatus.ALL, requestedListViewModel.currentStatus.value)
    }

    @Test
    fun `should select current status default value to requested`() {
        assertEquals(RequestStatus.REQUESTED, requestedListViewModel.currentStatus.value)
    }


    @Test
    fun `should return empty messages when active consents are empty`() {
        requestedListViewModel.updateFilterSelectedItem(0)
        assertEquals(R.string.no_new_consent_requests, requestedListViewModel.getNoConsentMessage())
    }

    @Test
    fun `should return empty messages when expired consents are empty`() {
        requestedListViewModel.updateFilterSelectedItem(1)
        assertEquals(R.string.no_expired_consent_requests, requestedListViewModel.getNoConsentMessage())
    }

    @Test
    fun `should return empty messages when denied consents are empty`() {
        requestedListViewModel.updateFilterSelectedItem(2)
        assertEquals(R.string.no_denied_consents, requestedListViewModel.getNoConsentMessage())
    }

    @Test
    fun `should return empty messages when consents are empty`() {
        requestedListViewModel.updateFilterSelectedItem(3)
        assertEquals(R.string.no_consent_requests, requestedListViewModel.getNoConsentMessage())
    }


    private fun dummyRequestedConsentsList(): List<Consent>? {
        return getData("requested_consents.json")
    }

    private fun getData(fileName: String) =
        Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))

    private fun filterConsents(status: RequestStatus): List<Consent> {
        return consentsListResponse.requests.asSequence().filter { it.status == status }.toList()
    }

    private fun setupConsentAPI() {

        var filteredConsents = consentsListResponse.requests
        if (requestedListViewModel.currentStatus.value != RequestStatus.ALL) {
            filteredConsents = filterConsents(requestedListViewModel.currentStatus.value!!)
        }
        val expectedResponse = ConsentsListResponse(filteredConsents, filteredConsents.size, 0)
        val filters = requestedListViewModel.currentStatus.value?.name
        `when`(repository.getConsents(10, 0, filters)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                consentsListResponse = expectedResponse
                callback.onResponse(call, Response.success(expectedResponse))
            }
    }

}