package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
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
import junit.framework.Assert.*
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
class RequestedConsentViewModelTest {

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

    @Mock
    private lateinit var consentsFetchObserver: Observer<PayloadResource<ConsentsListResponse>>

    private lateinit var consentViewModel: RequestedConsentViewModel

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = RequestedConsentViewModel(repository, preferenceRepo, credentialsRepo)

        consentsListResponse = Gson()
            .fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)

        `when`(repository.getConsents()).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(consentsListResponse))
            }

        consentViewModel.consentListResponse.observeForever(consentsFetchObserver)

        consentViewModel.getConsents()
        consentViewModel.filterConsents(consentsListResponse.requests)
    }

    @After
    fun tearDown() {
        consentViewModel.consentListResponse.removeObserver(consentsFetchObserver)
    }

    @Test
    fun `should Populate Filter Items For Requested Consents`() {
        `when`(resources.getString(R.string.status_active_requested_consents)).thenReturn("Active requested consents (%d)")
        `when`(resources.getString(R.string.status_expired_requested_consents)).thenReturn("Expired requested consents (%d)")
        `when`(resources.getString(R.string.status_denied_consent_requests)).thenReturn("Denied consent requests (%d)")
        `when`(resources.getString(R.string.status_all_requested_consents)).thenReturn("All requested consents (%d)")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources, ConsentFlow.REQUESTED_CONSENTS)

        assertEquals(dummyRequestedFilterList(), populatedFilterItems)
    }

    private fun dummyRequestedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("Active requested consents (1)")
        list.add("Expired requested consents (1)")
        list.add("Denied consent requests (0)")
        list.add("All requested consents (2)")
        return list
    }

    @Test
    fun `should Fetch Consents`() {

        verify(consentsFetchObserver, times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, times(1)).onChanged(Success(consentsListResponse))

    }

    @Test
    fun `should Filter Consents And Return Only Denied And RequestedList`() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        assertFalse(consentViewModel.requestedConsentsList.value!!.filter { it.status == RequestStatus.GRANTED }.count() > 0)
    }

    @Test
    fun `should Return Filter And Sorted List By Descending Order`() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        val first = consentViewModel.requestedConsentsList.value!!.first().getLastUpdated()
        val second = consentViewModel.requestedConsentsList.value!![1].getLastUpdated()
        assertTrue(first!!.after(second!!))
    }

    private fun dummyRequestedConsentsList(): List<Consent>? {
        return getData("requested_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))
}