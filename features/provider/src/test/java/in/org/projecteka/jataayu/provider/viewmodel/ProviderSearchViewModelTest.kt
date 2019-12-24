
import `in`.org.projecteka.jataayu.provider.model.Patient
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
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
    private lateinit var patientsInfoCall: Call<List<Patient>>

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
    fun shouldFetchProviders() {
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

    private fun getData(fileName : String) = Gson().fromJson<List<ProviderInfo>>(
        TestUtils.readFile(fileName), List::class.java)!!

    @Test
    fun shouldFetchPatients() {
        val identifier = "9876543210"
        val patients= Gson().fromJson<List<Patient>>(TestUtils.readFile("patient_info_from_providers.json"), List::class.java)!!
        Mockito.`when`(repository.getPatients(identifier)).thenReturn(patientsInfoCall)
        Mockito.`when`(patientsInfoCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<List<Patient>>
                callback.onResponse(patientsInfoCall, Response.success(patients))
            }
        viewModel.getPatients(identifier)
        Mockito.verify(repository).getPatients(identifier)
        Mockito.verify(patientsInfoCall).enqueue(ArgumentMatchers.any())
        assertEquals(patients, viewModel.patients.value)
    }
}