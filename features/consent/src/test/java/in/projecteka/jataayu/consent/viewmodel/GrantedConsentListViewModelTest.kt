package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ConsentListViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var credentialRepo: CredentialsRepository

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    @Mock
    private lateinit var consentsFetchObserver: Observer<PayloadResource<ConsentsListResponse>>

    private lateinit var consentViewModel: GrantedConsentListViewModel

    private lateinit var consentsListResponse: ConsentsListResponse

    private lateinit var grantedConsentList: List<Consent>



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = GrantedConsentListViewModel(repository,credentialRepo)

        val filters = null
        consentsListResponse = Gson()
            .fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)
        grantedConsentList = filterConsents(RequestStatus.GRANTED)

        `when`(repository.getConsents(10, 0, filters)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                consentsListResponse = ConsentsListResponse(grantedConsentList, consentsListResponse.totalCount, consentsListResponse.offset)
                callback.onResponse(call, Response.success(consentsListResponse))
            }

        consentViewModel.consentListResponse.observeForever(consentsFetchObserver)
    }

    @After
    fun tearDown() {
        consentViewModel.consentListResponse.removeObserver(consentsFetchObserver)
    }

    @Test
    fun `should Populate Filter Items For Granted Consents`() {

        `when`(resources.getString(R.string.status_all_granted_consents)).thenReturn("All Granted Consents")
        `when`(resources.getString(R.string.status_active_granted_consents)).thenReturn("Active granted consents")
        `when`(resources.getString(R.string.status_expired_granted_consents)).thenReturn("Expired granted consents")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources, ConsentFlow.GRANTED_CONSENTS)

        assertEquals(dummyGrantedFilterList(), populatedFilterItems)
    }

    private fun dummyGrantedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("All Granted Consents")
        list.add("Active granted consents")
        list.add("Expired granted consents")
        return list
    }


    @Test
    fun `should Fetch Consents`() {

        consentViewModel.getConsents(offset = 0)
        verify(consentsFetchObserver, Mockito.times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, Mockito.times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, Mockito.times(1)).onChanged(Success(consentsListResponse))

    }


    @Test
    fun `should Filter Consents And Return Only Granted RequestedList`() {

        val filters = null
        consentViewModel.getConsents( offset = 0)
        verify(repository).getConsents(10, 0, filters = filters)
        verify(call).enqueue(any())
    }


    @Test
    fun `should parse the response and return consent artifact response`() {

        val consentArtifactResponse = Gson()
            .fromJson(TestUtils.readFile("consent_artifact_response.json"), ConsentArtifactResponse::class.java)
        assertNotNull(consentArtifactResponse)

    }


    private fun dummyGrantedConsentsList(): List<Consent>? {
        return getData("granted_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))

    private fun filterConsents(status: RequestStatus) : List<Consent> {
        return consentsListResponse.requests.asSequence().filter { it.status == status }.toList()
    }
}