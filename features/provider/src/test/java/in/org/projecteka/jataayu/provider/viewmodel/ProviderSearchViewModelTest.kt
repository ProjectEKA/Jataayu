import `in`.org.projecteka.jataayu.provider.model.Patient
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.repository.ProviderRepository
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
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
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<ProviderInfo>> =
            moshi.adapter<List<ProviderInfo>>(List::class.java)
        val providers =
            jsonAdapter.fromJson(TestUtils.readFile("health_insurance_providers.json"))
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
    fun shouldFetchPatients() {
        val identifier = "9876543210"
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<Patient>> =
            moshi.adapter<List<Patient>>(List::class.java)
        val patients =
            jsonAdapter.fromJson(TestUtils.readFile("patient_info_from_providers.json"))
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