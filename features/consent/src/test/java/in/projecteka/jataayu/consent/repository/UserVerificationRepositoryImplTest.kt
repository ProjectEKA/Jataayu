package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
import `in`.projecteka.jataayu.core.model.UserVerificationRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserVerificationRepositoryImplTest {
    @Mock
    private lateinit var userVerificationApis: UserVerificationApis

    @Mock
    private lateinit var userVerificationRequest: UserVerificationRequest

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldCallUserVerificationApi() {
        UserVerificationRepositoryImpl(userVerificationApis).verifyUser(userVerificationRequest)
        Mockito.verify(userVerificationApis).getUserVerificationResponse(userVerificationRequest)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(userVerificationApis)
        Mockito.validateMockitoUsage()
    }
}