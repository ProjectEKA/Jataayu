package `in`.projecteka.jataayu.provider.viewmodel

import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.core.model.UnverifiedIdentifier
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.repository.UUIDRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ProviderSearchViewModelTest {

    @Mock
    private lateinit var repository: ProviderRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var uuidRepository: UUIDRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var providerInfoCall: Call<List<ProviderInfo>>

    @Mock
    private lateinit var responseCallback: ResponseCallback

    @Mock
    private lateinit var patientsInfoCall: Call<PatientDiscoveryResponse>

    @Mock
    private lateinit var userAccountsRepository: UserAccountsRepository


    private lateinit var viewModel: ProviderSearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProviderSearchViewModel(repository, userAccountsRepository, preferenceRepository, uuidRepository)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository, providerInfoCall, patientsInfoCall)
        validateMockitoUsage()
    }

    @Test
    fun shouldFetchProvidersIfProvidersListIsEmpty() {
        val query = "Health"
        val providers = getData("health_insurance_providers.json")
        `when`(repository.getProviders(query)).thenReturn(providerInfoCall)
        `when`(providerInfoCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<List<ProviderInfo>>
                callback.onResponse(providerInfoCall, Response.success(providers))
            }
        viewModel.getProviders(query, responseCallback)
        verify(repository).getProviders(query)
        verify(providerInfoCall).enqueue(ArgumentMatchers.any())
        assertEquals(providers, viewModel.providers.value)
    }


    @Test
    fun shouldNotFetchProvidersIfProvidersListIsNotEmpty() {
        val query = "Health"
        val providers = getData("health_insurance_providers.json")
        viewModel.providersList = ArrayList<ProviderInfo>(providers)

        viewModel.getProviders(query, responseCallback)
        verifyNoInteractions(repository)
    }

    private fun getData(fileName: String) = Gson().fromJson<List<ProviderInfo>>(TestUtils.readFile(fileName))

    @Test
    fun shouldFetchPatients() {
        val patients = setUpPatients()
        assertEquals(patients, viewModel.patientDiscoveryResponse.value)
    }


    @Test
    fun shouldReturnTrueIfAtLeastOneCareContextIsSelected() {
        setUpPatients()
        viewModel.patientDiscoveryResponse.value?.patient?.careContexts!![1].contextChecked = true
        Assert.assertTrue(viewModel.canLinkAccounts(viewModel.patientDiscoveryResponse.value?.patient?.careContexts!!).first)
    }

    @Test
    fun shouldReturnFalseIfNoCareContextIsSelected() {
        setUpPatients()
        viewModel.patientDiscoveryResponse.value?.patient?.careContexts!![0].contextChecked = false
        viewModel.patientDiscoveryResponse.value?.patient?.careContexts!![1].contextChecked = false
        Assert.assertFalse(viewModel.canLinkAccounts(viewModel.patientDiscoveryResponse.value?.patient?.careContexts!!).first)
    }

    private fun setUpPatients(): PatientDiscoveryResponse {
        `when`(uuidRepository.generateUUID()).thenReturn("276fa8f3-810b-49d0-b0b5-d1f760271255")
        var unverifiedIdentifiers = ArrayList<UnverifiedIdentifier>()
        unverifiedIdentifiers.add(UnverifiedIdentifier("XXX", "MR"))
        val request = Request(uuidRepository.generateUUID(), Hip("1", " Tata"), unverifiedIdentifiers)
        val patients = Gson().fromJson<PatientDiscoveryResponse>(
            TestUtils.readFile("patient_info_from_providers.json"),
            PatientDiscoveryResponse::class.java
        )!!
        `when`(repository.getPatientAccounts(request)).thenReturn(patientsInfoCall)
        `when`(patientsInfoCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<PatientDiscoveryResponse>
                callback.onResponse(patientsInfoCall, Response.success(patients))
            }
        viewModel.getPatientAccounts(request, responseCallback)
        verify(repository).getPatientAccounts(request)
        verify(patientsInfoCall).enqueue(ArgumentMatchers.any())
        return patients
    }
}