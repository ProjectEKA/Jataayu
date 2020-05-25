package `in`.projecteka.jataayu.user.account.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
        assertEquals(viewModel.redirectTo.value, RecoverCmidActivityViewModel.Show.READ_VALUES_SCREEN)
    }

    @Test
    fun `should set redirect value to display cmid screen`() {
        viewModel.onDisplayCmidRequest()
        assertEquals(viewModel.redirectTo.value, RecoverCmidActivityViewModel.Show.DISPLAY_CMID_SCREEN)
    }

    @Test
    fun `should set redirect value to no matching records screen`() {
        viewModel.onReviewRequest()
        assertEquals(viewModel.redirectTo.value, RecoverCmidActivityViewModel.Show.NO_OR_MULTIPLE_MATCHING_RECORDS)
    }
}