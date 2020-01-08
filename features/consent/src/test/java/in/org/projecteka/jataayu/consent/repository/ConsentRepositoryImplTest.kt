package `in`.org.projecteka.jataayu.consent.repository

import `in`.org.projecteka.jataayu.consent.remote.ConsentApis
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ConsentRepositoryImplTest{
    @Test
    fun shouldFetchConsents(){
        val consentApi = mock(ConsentApis::class.java)
        ConsentRepositoryImpl(consentApi).getConsents()
        verify(consentApi).getConsents()
    }
}