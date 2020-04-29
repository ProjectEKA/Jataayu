package `in`.projecteka.jataayu.consent.repository

import `in`.projecteka.jataayu.consent.remote.UserVerificationApis
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldCallUserVerificationApi() {
        val pin = "1234"
        UserVerificationRepositoryImpl(userVerificationApis).verifyUser(pin)
        Mockito.verify(userVerificationApis).getUserVerificationResponse(mapOf("pin" to pin))
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(userVerificationApis)
        Mockito.validateMockitoUsage()
    }
}