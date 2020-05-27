package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.RecoverCmidResponse
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.user.account.R
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RecoverCmidOtpFragmentViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RecoverCmidOtpFragmentViewModel

    @Mock
    private lateinit var respository: UserAccountsRepository

    @Mock
    private lateinit var recoverCmidResponseCall: Call<RecoverCmidResponse>

    @Mock
    private lateinit var resource: Resources

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = RecoverCmidOtpFragmentViewModel(respository)


    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(respository, recoverCmidResponseCall)
    }

    @Test
    fun `should fetch the cmid by verifying the otp`() {
        val sessionId = UUID.randomUUID().toString()
        viewModel.otpText.set("123456")
        val cmIdJsonResponse = """{"cmId": "string"}"""
        val recoverCmidResponse = Gson().fromJson(cmIdJsonResponse, RecoverCmidResponse::class.java)
        val verifyOTPRequest = viewModel.getVerifyOTPRequestPayload(sessionId)
        Mockito.`when`(respository.recoverCmid(verifyOTPRequest)).thenReturn(recoverCmidResponseCall)
        Mockito.`when`(recoverCmidResponseCall.enqueue(Mockito.any())).then {
            val callback = it.arguments[0] as Callback<RecoverCmidResponse>
            callback.onResponse(recoverCmidResponseCall, Response.success(recoverCmidResponse))
        }

        viewModel.verifyOtp(sessionId)
        Mockito.verify(respository).recoverCmid(verifyOTPRequest)
        Mockito.verify(recoverCmidResponseCall).enqueue(any())
    }

    @Test
    fun `should create a correct payload`() {
        val sessionId = UUID.randomUUID().toString()
        viewModel.otpText.set("123456")
        val verifyOTPRequest = viewModel.getVerifyOTPRequestPayload(sessionId)
        assertEquals("123456", verifyOTPRequest.value)
        assertEquals(sessionId, verifyOTPRequest.sessionId)
    }

    @Test
    fun `should disable the validate button initially`() {
        assertFalse(viewModel.validateButtonEnabled.get())
    }

    @Test
    fun `should disable the validate button when entered OTP is not 6 digits `() {
        viewModel.onTextChanged("1234", 0, 0, 4)
        assertFalse(viewModel.validateButtonEnabled.get())
    }

    @Test
    fun `should set the invalid otp message when otp verification failed with invalid otp`() {

        val message = "Invalid OTP entered. Please try again"
        Mockito.`when`(resource.getString(R.string.invalid_otp)).thenReturn(message)
        val error = `in`.projecteka.jataayu.network.model.Error(1003,  message)
        viewModel.onVerifyOTPResponseFailure(error, resource)
        assertEquals(null, viewModel.otpText.get())
        assertEquals(message, viewModel.errorLbl.get())
    }

    @Test
    fun `should set the expired otp message when otp verification failed with expired otp`() {

        val message = "OTP expired."
        Mockito.`when`(resource.getString(R.string.otp_expired)).thenReturn(message)
        val error = `in`.projecteka.jataayu.network.model.Error(1004, message)
        viewModel.onVerifyOTPResponseFailure(error, resource)
        assertEquals(null, viewModel.otpText.get())
        assertEquals(message, viewModel.errorLbl.get())
    }

    @Test
    fun `should set the maximum attempt limit exceeded otp message when otp verification attempt limit reached to 5`() {
        val message = "You have exceeded the invalid attempts limit, Please try later"
        Mockito.`when`(resource.getString(R.string.exceeded_otp_attempt_limit)).thenReturn(message)
        val error = `in`.projecteka.jataayu.network.model.Error(1035, message)
        viewModel.onVerifyOTPResponseFailure(error, resource)
        assertEquals(null, viewModel.otpText.get())
        assertEquals(message, viewModel.errorLbl.get())
    }
}