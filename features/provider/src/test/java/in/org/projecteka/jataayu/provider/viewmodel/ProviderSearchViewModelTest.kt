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
    private lateinit var call: Call<List<ProviderInfo>>

    private lateinit var viewModel: ProviderSearchViewModel

    private var query = "Health"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProviderSearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repository, call)
        Mockito.validateMockitoUsage()
    }

    @Test
    fun shouldFetchProviders() {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<ProviderInfo>> =
            moshi.adapter<List<ProviderInfo>>(List::class.java)
        val providers =
            jsonAdapter.fromJson(TestUtils.readFile("health_insurance_providers.json"))
        Mockito.`when`(repository.getProvider(query)).thenReturn(call)
        Mockito.`when`(call.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<List<ProviderInfo>>
                callback.onResponse(call, Response.success(providers))
            }
        viewModel.getProvider(query)
        Mockito.verify(repository).getProvider(query)
        Mockito.verify(call).enqueue(ArgumentMatchers.any())
        assertEquals(providers, viewModel.providers.value)
    }
}