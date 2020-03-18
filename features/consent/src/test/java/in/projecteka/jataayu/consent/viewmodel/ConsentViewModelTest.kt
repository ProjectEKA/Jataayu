package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
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
x
@RunWith(MockitoJUnitRunner::class)
class ConsentViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    private lateinit var consentViewModel: ConsentViewModel

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = ConsentViewModel(repository)

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

    @Test
    fun shouldPopulateFilterItemsForGrantedConsents() {

        `when`(resources.getString(R.string.status_active_granted_consents)).thenReturn("Active granted consents (%d)")
        `when`(resources.getString(R.string.status_expired_granted_consents)).thenReturn("Expired granted consents (%d)")
        `when`(resources.getString(R.string.status_all_granted_consents)).thenReturn("All Granted Consents (%d)")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources, ConsentFlow.GRANTED_CONSENTS)

        assertEquals(dummyGrantedFilterList(), populatedFilterItems)
    }

    private fun dummyRequestedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("Active requested consents (1)")
        list.add("Expired requested consents (1)")
        list.add("Denied consent requests (0)")
        list.add("All requested consents (2)")
        return list
    }

    private fun dummyGrantedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("Active granted consents (1)")
        list.add("Expired granted consents (1)")
        list.add("All Granted Consents (2)")
        return list
    }


    @Test
    fun shouldFetchConsents() {
        verify(repository).getConsents()
        verify(call).enqueue(any())
        assertEquals(Success(consentsListResponse), consentViewModel.consentListResponse.value)
    }

    @Test
    fun shouldFilterConsents() {
        consentViewModel.filterConsents(consentsListResponse.requests)
        assertEquals(dummyRequestedConsentsList(), consentViewModel.requestedConsentsList.value)
        assertEquals(dummyGrantedConsentsList(), consentViewModel.grantedConsentsList.value)
    }

    private fun dummyRequestedConsentsList(): List<Consent>? {
        return getData("requested_consents.json")
    }

    private fun dummyGrantedConsentsList(): List<Consent>? {
        return getData("granted_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))
}