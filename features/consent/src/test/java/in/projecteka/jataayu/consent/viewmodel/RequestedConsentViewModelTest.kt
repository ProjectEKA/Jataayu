package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
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
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    private lateinit var consentViewModel: RequestedConsentViewModel

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = RequestedConsentViewModel(repository)

        consentsListResponse = Gson()
            .fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)

        `when`(repository.getConsents()).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(consentsListResponse))
            }

        consentViewModel.getConsents()
        consentViewModel.filterConsents(consentsListResponse.requests)
    }

    @Test
    fun shouldPopulateFilterItemsForRequestedConsents() {
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
    fun shouldFetchConsents() {
        verify(repository).getConsents()
        verify(call).enqueue(any())
        assertEquals(Success(consentsListResponse), consentViewModel.consentListResponse.value)
    }

    @Test
    fun shouldFilterConsentsAndReturnOnlyDeniedAndRequestedList() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        assertFalse(consentViewModel.requestedConsentsList.value!!.filter { it.status == RequestStatus.GRANTED }.count() > 0)
    }

    @Test
    fun shouldReturnFilterAndSortedListByDescendingOrder() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        val first =  consentViewModel.requestedConsentsList.value!!.first().getLastUpdated()
        val second = consentViewModel.requestedConsentsList.value!![1].getLastUpdated()
        assertTrue(first!!.after(second!!))
    }

    private fun dummyRequestedConsentsList(): List<Consent>? {
        return getData("requested_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))
}