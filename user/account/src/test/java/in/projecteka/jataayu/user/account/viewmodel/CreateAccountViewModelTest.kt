package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call

@RunWith(MockitoJUnitRunner::class)
class CreateAccountViewModelTest {

    private lateinit var viewModel: CreateAccountViewModel

    @Mock
    private lateinit var repository: UserAccountsRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var createAccountAccountCall: Call<CreateAccountResponse>


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = CreateAccountViewModel()
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repository, createAccountAccountCall)
        Mockito.validateMockitoUsage()
    }


    @Test
    fun `should set error flag when name is empty`() {
        viewModel.inputFullName.set("")
        viewModel.validateName()
        assertEquals(true, viewModel.showErrorName.get())
    }

    @Test
    fun `should not set error flag when name is empty`() {
        viewModel.inputFullName.set("maabu")
        viewModel.validateName()
        assertEquals(false, viewModel.showErrorName.get())
    }

    @Test
    fun `should set error flag when gender is not checked`() {
        viewModel.onCheckedChanged(null, -1)
        assertEquals(true, viewModel.showErrorGender.get())
    }

    @Test
    fun `should enable submit button when all the required fields entered`() {

        viewModel.inputFullName.set("mabu")
        viewModel.onCheckedChanged(null, 2)
        assertEquals(true,  viewModel.validateFields())
        assertEquals(true, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when gender not selected`() {

        viewModel.inputFullName.set("mabu")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when name not entered`() {

        viewModel.inputFullName.set("")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when username is not entered`() {
        viewModel.inputFullName.set("mabu")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when password is not entered`() {
        viewModel.inputFullName.set("maabu")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `test ayushman Id should return valid if id starts with letter P `() {
        viewModel.inputAyushmanIdLbl.set("PAYUSh123")
        viewModel.validateAyushmanId()
        assertTrue(!viewModel.showErrorAyushmanId.get())
    }

    @Test
    fun `test ayushman Id should return valid with letter P with ignoring case sensitive `() {
        viewModel.inputAyushmanIdLbl.set("P123456ID")
        viewModel.validateAyushmanId()
        assertTrue(!viewModel.showErrorAyushmanId.get())
    }

    @Test
    fun `test ayushman Id should return invalid if id must be 9 characters long`() {
        viewModel.inputAyushmanIdLbl.set("P123456ID")
        viewModel.validateAyushmanId()
        assertTrue(!viewModel.showErrorAyushmanId.get())
    }

    @Test
    fun `test ayushman Id should return invalid if id not starts with letter P `() {
        viewModel.inputAyushmanIdLbl.set("JAYUSh123")
        viewModel.validateAyushmanId()
        assertFalse(!viewModel.showErrorAyushmanId.get())
    }


    @Test
    fun `test ayushman Id should return valid if id must be 9 characters long`() {
        viewModel.inputAyushmanIdLbl.set("P123456IDK")
        viewModel.validateAyushmanId()
        assertFalse(!viewModel.showErrorAyushmanId.get())
    }


    @Test
    fun `test years to populate of list of 120 years`() {

        val yearsToPopulate = viewModel.getYearsToPopulate()
        assertEquals("yyyy",yearsToPopulate[0])
        assertEquals("2020",yearsToPopulate[1])
        assertEquals("1901",yearsToPopulate[120])
    }
}