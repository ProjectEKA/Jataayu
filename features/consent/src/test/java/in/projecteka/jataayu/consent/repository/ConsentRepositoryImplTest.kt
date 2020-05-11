package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.ConsentApis
import org.junit.Test
import org.mockito.Mockito.mock

class ConsentRepositoryImplTest{
    @Test
    fun shouldFetchConsents(){
        val consentApi = mock(ConsentApis::class.java)
//        ConsentRepositoryImpl(consentApi).getConsents()
//        verify(consentApi).getConsents()
    }
}