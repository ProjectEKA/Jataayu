package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.provider.remote.ProviderApis
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ProviderRepositoryImplTest {

    @Mock
    lateinit var providerSearchApi : ProviderApis

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldCallGetProviderApi() {
        val query = "Max"
        ProviderRepositoryImpl(providerSearchApi).getProviders(query)
        verify(providerSearchApi).getProviders(query)
    }

    @Test
    fun shouldCallGetPatientsApi() {
        val identifier = "9876543210"
        ProviderRepositoryImpl(providerSearchApi).getPatients(identifier)
        verify(providerSearchApi).getPatients(identifier)
    }
}