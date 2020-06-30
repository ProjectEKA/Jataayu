package `in`.projecteka.jataayu.util.repository

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.edit
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.text.Typography.times

@RunWith(MockitoJUnitRunner::class)
class CredentialsRepositoryImplTest {

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    private lateinit var credentialsRepositoryImpl: CredentialsRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        credentialsRepository.refreshTokenExpiresIn=TimeUnit.MILLISECONDS.toSeconds(Date().time) + 3600
        credentialsRepository.accessTokenExpiresIn=TimeUnit.MILLISECONDS.toSeconds(Date().time) + 1800

        credentialsRepositoryImpl = MockCredentialRepository(sharedPreferences, Date())
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
    }

    @After
    fun tearDown() {
        validateMockitoUsage()
    }

    @Test
    fun `test should return true if refresh token is expired`() {
        val refreshTokenExpiry = TimeUnit.MILLISECONDS.toSeconds(Date().time) + 3600

        Mockito.`when`(credentialsRepositoryImpl.isRefreshTokeExpired()).thenReturn(true)
    }

    @Test
    fun isAccessTokeExpired() {
    }
}

class MockCredentialRepository(private val sharedPreferences: SharedPreferences, private val date: Date): CredentialsRepositoryImpl(sharedPreferences) {

    override fun isRefreshTokeExpired(): Boolean {
        return if(refreshTokenExpiresIn != 0L) {
            refreshTokenExpiresIn < TimeUnit.MILLISECONDS.toSeconds(date.time)
        } else {
            false
        }
    }

    override fun isAccessTokeExpired(): Boolean {
        return if(accessTokenExpiresIn != 0L) {
            accessTokenExpiresIn < TimeUnit.MILLISECONDS.toSeconds(date.time)
        } else {
            false
        }
    }
}