package `in`.org.projecteka.jataayu.provider.repository

import `in`.org.projecteka.jataayu.provider.remote.ProviderSearchApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ProviderRepositoryImplTest {

    @Mock
    lateinit var providerSearchApi : ProviderSearchApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldCallGetProviderApi() {
        val query = "Max"
        ProviderRepositoryImpl(providerSearchApi).getProvider(query)
        verify(providerSearchApi).getProvider(query)
    }
}