package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
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
    private lateinit var call: Call<ConsentArtifactResponse>

    @Mock
    private lateinit var consentsFetchObserver: Observer<PayloadResource<ConsentArtifactResponse>>

    private lateinit var consentViewModel: GrantedConsentListViewModel

    private lateinit var consentArtifactResponse: ConsentArtifactResponse

    private lateinit var grantedConsentList: List<Consent>



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = GrantedConsentListViewModel(repository,credentialRepo)

        consentArtifactResponse = Gson()
            .fromJson(TestUtils.readFile("consent_artifact_response.json"), ConsentArtifactResponse::class.java)

        consentViewModel.consentArtifactResponse.observeForever(consentsFetchObserver)
    }

    @After
    fun tearDown() {
        consentViewModel.consentArtifactResponse.removeObserver(consentsFetchObserver)
    }

    @Test
    fun `should Populate Filter Items For Granted Consents`() {

        `when`(resources.getString(R.string.status_all_consents_artifacts)).thenReturn("All consents")
        `when`(resources.getString(R.string.status_active_granted_consents)).thenReturn("Granted consents")
        `when`(resources.getString(R.string.status_revoked_consents)).thenReturn("Revoked consents")
        `when`(resources.getString(R.string.status_expired_granted_consents)).thenReturn("Expired consents")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources)

        assertEquals(dummyGrantedFilterList(), populatedFilterItems)
    }

    private fun dummyGrantedFilterList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("Granted consents")
        list.add("Revoked consents")
        list.add("Expired consents")
        list.add("All consents")
        return list
    }


    @Test
    fun `should Fetch all Consents artifacts`() {

        consentViewModel.updateFilterSelectedItem( GrantedConsentListViewModel.INDEX_ALL)
        getConsentArtifactSetup()
        consentViewModel.getConsents(offset = 0)
        verify(consentsFetchObserver, Mockito.times(1)).onChanged(Loading(true))
        verify(consentsFetchObserver, Mockito.times(1)).onChanged(Loading(false))
        verify(consentsFetchObserver, Mockito.times(1)).onChanged(Success(consentArtifactResponse))

    }


    @Test
    fun `should Filter Consents And Return Only Granted ConsentList`() {

        consentViewModel.updateFilterSelectedItem(GrantedConsentListViewModel.INDEX_GRANTED)
        getConsentArtifactSetup()
        consentViewModel.getConsents( offset = 0)
        verify(repository).getConsentsArtifactList(GrantedConsentListViewModel.LIMIT, 0, consentViewModel.currentStatus.value!!)
        verify(call).enqueue(any())
        assertEquals(0, consentArtifactResponse.getArtifacts().filter { it.status != RequestStatus.GRANTED }.size)
    }

    @Test
    fun `should Filter Consents And Return Only Revoked ConsentList`() {

        consentViewModel.updateFilterSelectedItem(GrantedConsentListViewModel.INDEX_REVOKED)
        getConsentArtifactSetup()
        consentViewModel.getConsents( offset = 0)
        verify(repository).getConsentsArtifactList(GrantedConsentListViewModel.LIMIT, 0, consentViewModel.currentStatus.value!!)
        verify(call).enqueue(any())
        assertEquals(0, consentArtifactResponse.getArtifacts().filter { it.status != RequestStatus.REVOKED }.size)
    }

    @Test
    fun `should Filter Consents And Return Only Expired ConsentList`() {

        consentViewModel.updateFilterSelectedItem(GrantedConsentListViewModel.INDEX_EXPIRED)
        getConsentArtifactSetup()
        consentViewModel.getConsents( offset = 0)
        verify(repository).getConsentsArtifactList(GrantedConsentListViewModel.LIMIT, 0, consentViewModel.currentStatus.value!!)
        verify(call).enqueue(any())
        assertEquals(0, consentArtifactResponse.getArtifacts().filter { it.status != RequestStatus.EXPIRED }.size)
    }


    @Test
    fun `should parse the response and return consent artifact response`() {

        val consentArtifactResponse = Gson()
            .fromJson(TestUtils.readFile("consent_artifact_response.json"), ConsentArtifactResponse::class.java)
        assertNotNull(consentArtifactResponse)
    }

    @Test
    fun `should change current status to granted when filter is selected to 1st item`() {
        consentViewModel.updateFilterSelectedItem(0)
        assertEquals(RequestStatus.GRANTED, consentViewModel.currentStatus.value)
    }

    @Test
    fun `should change current status to revoked when filter is selected to 2nd item`() {
        consentViewModel.updateFilterSelectedItem(1)
        assertEquals(RequestStatus.REVOKED, consentViewModel.currentStatus.value)
    }

    @Test
    fun `should change current status to expired when filter is selected to 3rd item`() {
        consentViewModel.updateFilterSelectedItem(2)
        assertEquals(RequestStatus.EXPIRED, consentViewModel.currentStatus.value)
    }

    @Test
    fun `should change current status to all when filter is selected to 4th item`() {
        consentViewModel.updateFilterSelectedItem(3)
        assertEquals(RequestStatus.ALL, consentViewModel.currentStatus.value)
    }


    private fun dummyGrantedConsentsList(): List<Consent>? {
        return getData("granted_consents.json")
    }

    private fun getData(fileName: String) = Gson().fromJson<List<Consent>>(TestUtils.readFile(fileName))

    private fun filterConsents(status: RequestStatus) : List<Consent> {
        return consentArtifactResponse.getArtifacts().asSequence().filter { it.status == status }.toList()
    }

    private fun getGrantedConsentDetailsList(consents: List<Consent>) : List<GrantedConsentDetailsResponse> {
        return consents.map { GrantedConsentDetailsResponse(it.status, it, "") }
    }

    private fun getConsentArtifactSetup() {

        `when`(repository.getConsentsArtifactList(GrantedConsentListViewModel.LIMIT, 0, consentViewModel.currentStatus.value!!)).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentArtifactResponse>
                grantedConsentList = filterConsents(consentViewModel.currentStatus.value!!)
                val grantedConsentDetailsResponse = getGrantedConsentDetailsList(grantedConsentList)
                val response = ConsentArtifactResponse(null,
                    grantedConsentDetailsResponse,
                    consentArtifactResponse.limit,
                    consentArtifactResponse.size,
                    consentArtifactResponse.signature)
                consentArtifactResponse = response
                callback.onResponse(call, Response.success(response))
            }
    }
}