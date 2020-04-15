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
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert
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
class GrantedConsentViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    private lateinit var consentViewModel: GrantedConsentViewModel

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = GrantedConsentViewModel(repository)

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
    fun shouldPopulateFilterItemsForGrantedConsents() {

        `when`(resources.getString(R.string.status_all_granted_consents)).thenReturn("All Granted Consents (%d)")
        `when`(resources.getString(R.string.status_active_granted_consents)).thenReturn("Active granted consents (%d)")
        `when`(resources.getString(R.string.status_expired_granted_consents)).thenReturn("Expired granted consents (%d)")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources, ConsentFlow.GRANTED_CONSENTS)

        assertEquals(dummyGrantedFilterList(), populatedFilterItems)
    }

    private fun dummyGrantedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("All Granted Consents (2)")
        list.add("Active granted consents (1)")
        list.add("Expired granted consents (1)")
        return list
    }


    @Test
    fun shouldFetchConsents() {
        verify(repository).getConsents()
        verify(call).enqueue(any())
        assertEquals(Success(consentsListResponse), consentViewModel.consentListResponse.value)
    }


    @Test
    fun shouldFilterConsentsAndReturnOnlyGrantedRequestedList() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        assertFalse(consentViewModel.grantedConsentsList.value!!.filter { it.status == RequestStatus.REQUESTED || it.status == RequestStatus.DENIED }.count() > 0)
    }

    @Test
    fun shouldReturnFilterAndSortedListByDescendingOrder() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        val first =  consentViewModel.requestedConsentsList.value!!.first().getLastUpdated()
        val second = consentViewModel.requestedConsentsList.value!![1].getLastUpdated()
        assertTrue(first!!.after(second!!))
    }

    private fun dummyGrantedConsentsList(): List<Consent>? {
        return getData("granted_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))
}