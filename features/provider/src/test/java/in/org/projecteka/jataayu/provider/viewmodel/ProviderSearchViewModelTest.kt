package `in`.org.projecteka.jataayu.provider.viewmodel
import TestUtils
import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.util.extension.fromJson
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
import org.mockito.Mockito
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ProviderSearchViewModelTest {

    @Mock
    private lateinit var repository: ProviderRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var providerInfoCall: Call<List<ProviderInfo>>

    @Mock
    private lateinit var patientsInfoCall: Call<PatientDiscoveryResponse>

    private lateinit var viewModel: ProviderSearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProviderSearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repository, providerInfoCall, patientsInfoCall)
        Mockito.validateMockitoUsage()
    }

    @Test
    fun shouldFetchProvidersIfProvidersListIsEmpty() {
        val query = "Health"
        val providers = getData("health_insurance_providers.json")
        Mockito.`when`(repository.getProviders(query)).thenReturn(providerInfoCall)
        Mockito.`when`(providerInfoCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<List<ProviderInfo>>
                callback.onResponse(providerInfoCall, Response.success(providers))
            }
        viewModel.getProviders(query)
        Mockito.verify(repository).getProviders(query)
        Mockito.verify(providerInfoCall).enqueue(ArgumentMatchers.any())
        assertEquals(providers, viewModel.providers.value)
    }


    @Test
    fun shouldNotFetchProvidersIfProvidersListIsNotEmpty() {
        val query = "Health"
        val providers = getData("health_insurance_providers.json")
        viewModel.providersList = ArrayList<ProviderInfo>(providers)

        viewModel.getProviders(query)
        verifyNoInteractions(repository)
    }

    private fun getData(fileName : String) = Gson().fromJson<List<ProviderInfo>>(TestUtils.readFile(fileName))

    @Test
    fun shouldFetchPatients() {
        val patients = setUpPatients()
        assertEquals(patients, viewModel.patientDiscoveryResponse.value)
    }


    @Test
    fun shouldReturnTrueIfAtLeastOneCareContextIsSelected() {
        setUpPatients()
        viewModel.patientDiscoveryResponse.value?.patient?.careContexts!![1].contextChecked = true
        Assert.assertTrue(viewModel.canLinkAccounts())
    }

    @Test
    fun shouldReturnFalseIfNoCareContextIsSelected() {
        setUpPatients()
        viewModel.patientDiscoveryResponse.value?.patient?.careContexts!![0].contextChecked = false
        viewModel.patientDiscoveryResponse.value?.patient?.careContexts!![1].contextChecked = false
        Assert.assertFalse(viewModel.canLinkAccounts())
    }

    private fun setUpPatients(): PatientDiscoveryResponse {
        val identifier = "9876543210"
        val patients = Gson().fromJson<PatientDiscoveryResponse>(
            TestUtils.readFile("patient_info_from_providers.json"),
            PatientDiscoveryResponse::class.java
        )!!
        Mockito.`when`(repository.getPatientAccounts(identifier)).thenReturn(patientsInfoCall)
        Mockito.`when`(patientsInfoCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<PatientDiscoveryResponse>
                callback.onResponse(patientsInfoCall, Response.success(patients))
            }
        viewModel.getPatientAccounts(identifier)
        Mockito.verify(repository).getPatientAccounts(identifier)
        Mockito.verify(patientsInfoCall).enqueue(ArgumentMatchers.any())
        return patients
    }
}