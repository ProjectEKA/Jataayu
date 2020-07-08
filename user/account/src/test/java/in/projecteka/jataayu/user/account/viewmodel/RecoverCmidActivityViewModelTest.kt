package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecoverCmidActivityViewModelTest {

    private lateinit var viewModel: RecoverCmidActivityViewModel


    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = RecoverCmidActivityViewModel()
    }

    @Test
    fun `should set redirect value to read values screen initially`() {
        viewModel.init()
        assertEquals(
            viewModel.redirectTo.value,
            RecoverCmidActivityViewModel.Show.READ_VALUES_SCREEN
        )
    }

    @Test
    fun `should set redirect value to display cmid screen`() {
        val recoverCmidResponse = RecoverCmidResponse("vk2704201")
        viewModel.onDisplayCmidRequest(recoverCmidResponse)
        assertEquals(
            viewModel.redirectTo.value,
            RecoverCmidActivityViewModel.Show.DISPLAY_CMID_SCREEN
        )
        assertEquals(recoverCmidResponse, viewModel.recoverCmidResponse)
    }

    @Test
    fun `should set redirect value to no matching records screen`() {
        viewModel.onReviewRequest()
        assertEquals(
            viewModel.redirectTo.value,
            RecoverCmidActivityViewModel.Show.NO_OR_MULTIPLE_MATCHING_RECORDS
        )
    }

    @Test
    fun `should set redirect value to otp screen`() {
        val generateOtpResponseJson =
            """{"sessionId": "3fa85f64-5717-4562-b3fc-2c963f66afa6","otpMedium": "MOBILE","otpMediumValue": 9999999999,"expiryInMinutes": 5}"""
        val generateOtpResponse = Gson().fromJson(generateOtpResponseJson, GenerateOTPResponse::class.java)
        val recovercmIdRequest = RecoverCmidRequest(Name("mabu", "", ""), "M", DateOfBirth(null, null, 1966), null, null)
        viewModel.onOTPRequest(recovercmIdRequest, generateOtpResponse)
        assertEquals(
            viewModel.redirectTo.value,
            RecoverCmidActivityViewModel.Show.OTP_SCREEN
        )
        assertEquals(generateOtpResponse, viewModel.generateOTPResponse)
        assertEquals(recovercmIdRequest, viewModel.otpRequest)
    }
}