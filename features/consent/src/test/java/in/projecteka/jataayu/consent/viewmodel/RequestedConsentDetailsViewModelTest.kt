package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HiType
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifact
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RequestedConsentDetailsViewModelTest {

    private lateinit var viewModel: RequestedConsentDetailsViewModel

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var linkedAccountCall: Call<LinkedAccountsResponse>

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var linkedAccountObserver: Observer<in PayloadResource<LinkedAccountsResponse>>

    @Mock
    private lateinit var grantedConsentObserver: Observer<in PayloadResource<ConsentArtifactResponse>>

    @Mock
    private lateinit var denyConsentObserver: Observer<in PayloadResource<Void>>

    @Mock
    private lateinit var grantConsentCall: Call<ConsentArtifactResponse>

    @Mock
    private lateinit var denyConsentCall: Call<Void>

    private lateinit var linkedAccountsResponse: LinkedAccountsResponse


    private lateinit var consent: Consent
    private lateinit var grantedConsentResponse: ConsentArtifactResponse

    @Before
    fun setup() {
        viewModel = RequestedConsentDetailsViewModel(
            preferenceRepository = preferenceRepository,
            credentialsRepository = credentialsRepository,
            repository = repository
        )
        linkedAccountsResponse = Gson()
            .fromJson(TestUtils.readFile("linked_account.json"), LinkedAccountsResponse::class.java)
        consent = Gson()
            .fromJson(
                TestUtils.readFile("consent_list_response.json"),
                ConsentsListResponse::class.java
            ).requests.first()
        grantedConsentResponse = Gson()
            .fromJson(
                TestUtils.readFile("consent_artifact_response.json"),
                ConsentArtifactResponse::class.java
            )

        `when`(credentialsRepository.consentTemporaryToken).thenReturn("abc")

        viewModel.linkedAccountsResponse.observeForever(linkedAccountObserver)
        viewModel.consentArtifactResponse.observeForever(grantedConsentObserver)
        viewModel.consentDenyResponse.observeForever(denyConsentObserver)
    }

    @Test
    fun `test should fetch linked accounts`() {

        `when`(repository.getLinkedAccounts()).thenReturn(linkedAccountCall)
        `when`(linkedAccountCall.enqueue(any())).then {
            val callback = it.arguments[0] as Callback<LinkedAccountsResponse>
            callback.onResponse(linkedAccountCall, Response.success(linkedAccountsResponse))
        }
        viewModel.getLinkedAccounts()
        verify(repository).getLinkedAccounts()
        verify(linkedAccountCall).enqueue(any())
        verify(linkedAccountObserver, times(1)).onChanged(Loading(true))
        verify(linkedAccountObserver, times(1)).onChanged(Loading(false))
        verify(linkedAccountObserver, times(1)).onChanged(Success(linkedAccountsResponse))

    }

    @Test
    fun `test should return one consent artifact if one care context is checked`() {

        linkedAccountsResponse.linkedPatient.links.first().careContexts.first().contextChecked =
            true
        val hiTypes = consent.hiTypes.map { HiType(it, true) } as ArrayList<HiType>
        val consentArtifact = viewModel.getConsentArtifact(
            linkedAccountsResponse.linkedPatient.links,
            hiTypes, consent.permission
        )
        assertEquals(consentArtifact.size, 1)
        assertNotNull(consentArtifact.first().careReferences)
        assertEquals(consentArtifact.first().hiTypes.size, hiTypes.size)
        assertNotNull(consentArtifact.first().hip)
    }

    @Test
    fun `test should return empty artifacts if care context is not checked`() {

        val hiTypes = consent.hiTypes.map { HiType(it, true) } as ArrayList<HiType>
        val consentArtifact = viewModel.getConsentArtifact(
            linkedAccountsResponse.linkedPatient.links,
            hiTypes, consent.permission
        )
        assertTrue(consentArtifact.isEmpty())
    }

    @Test
    fun `test should create consent artifact request`() {
        val consentArtifact = getConsentArtifact()
        val consentArtifactRequest = viewModel.getConsentArtifactRequest(getConsentArtifact())
        assertEquals(consentArtifactRequest.consents, consentArtifact)
    }

    @Test
    fun `test consent grant should be success`() {

        val consentArtifactRequest = viewModel.getConsentArtifactRequest(getConsentArtifact())
        `when`(
            repository.grantConsent(
                "123",
                consentArtifactRequest,
                credentialsRepository.consentTemporaryToken
            )
        ).thenReturn(grantConsentCall)
        `when`(grantConsentCall.enqueue(any())).then {
            val callback = it.arguments[0] as Callback<ConsentArtifactResponse>
            callback.onResponse(grantConsentCall, Response.success(grantedConsentResponse))
        }
        viewModel.grantConsent("123", getConsentArtifact())
        verify(repository).grantConsent("123",consentArtifactRequest, credentialsRepository.consentTemporaryToken)
        verify(grantConsentCall).enqueue(any())
        verify(grantedConsentObserver, times(1)).onChanged(Loading(true))
        verify(grantedConsentObserver, times(1)).onChanged(Loading(false))
        verify(grantedConsentObserver, times(1)).onChanged(Success(grantedConsentResponse))
    }

    @Test
    fun `test consent deny should be success`() {

        `when`(
            repository.denyConsent(
                "123"
            )
        ).thenReturn(denyConsentCall)
        `when`(denyConsentCall.enqueue(any())).then {
            val callback = it.arguments[0] as Callback<Void>
            callback.onResponse(denyConsentCall, Response.success(null))
        }
        viewModel.denyConsent("123")
        verify(repository).denyConsent("123")
        verify(denyConsentCall).enqueue(any())
        verify(denyConsentObserver, times(1)).onChanged(Loading(true))
        verify(denyConsentObserver, times(1)).onChanged(Loading(false))
        verify(denyConsentObserver, times(1)).onChanged(Success(null))
    }


    @Test
    fun `test bottom button should be invisible when consent status is expired`() {
        assertFalse(viewModel.canShowBottomButtons(RequestStatus.EXPIRED))
    }

    @Test
    fun `test bottom button should be invisible when consent status is denied`() {
        assertFalse(viewModel.canShowBottomButtons(RequestStatus.DENIED))
    }

    @Test
    fun `test bottom button should be visible when consent status is denied`() {
        assertTrue(viewModel.canShowBottomButtons(RequestStatus.REQUESTED))
    }


    private fun getConsentArtifact(): List<ConsentArtifact> {
        val hiTypes = consent.hiTypes.map { HiType(it, true) } as ArrayList<HiType>
        return viewModel.getConsentArtifact(
            linkedAccountsResponse.linkedPatient.links,
            hiTypes, consent.permission
        )
    }
}