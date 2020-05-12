package `in`.projecteka.resetpassword

import `in`.projecteka.resetpassword.viewmodel.ResetPasswordActivityViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ResetPasswordActivityViewModelTest {

    private lateinit var viewModel: ResetPasswordActivityViewModel

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = ResetPasswordActivityViewModel()
    }

    @Test
    fun `should set redirect value to first screen initially`() {
        viewModel.init()
    }

    @Test
    fun `should set redirect value to SECOND`() {
        viewModel.onOtpFragmentRedirectRequest()
        assertEquals(viewModel.redirectTo.value, ResetPasswordActivityViewModel.Show.SECOND_SCREEN)
    }

    @Test
    fun `should set redirect value to THIRD`() {
        viewModel.onVerifyOtpRedirectRequest()
        assertEquals(viewModel.redirectTo.value, ResetPasswordActivityViewModel.Show.THIRD_SECREEN)
    }
}