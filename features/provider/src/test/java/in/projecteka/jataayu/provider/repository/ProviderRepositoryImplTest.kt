package `in`.projecteka.jataayu.provider.repository

import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.core.model.UnverifiedIdentifier
import `in`.projecteka.jataayu.provider.remote.ProviderApis
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
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
        var unverifiedIdentifiers = ArrayList<UnverifiedIdentifier>()
        unverifiedIdentifiers.add(UnverifiedIdentifier("XXX", "MR"))
        val request = Request(ArgumentMatchers.anyString(), Hip("1", " Tata"), unverifiedIdentifiers)
        ProviderRepositoryImpl(providerSearchApi).getPatientAccounts(request)
        verify(providerSearchApi).getPatientAccounts(request)
    }
}