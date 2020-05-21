package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.*
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

@RunWith(MockitoJUnitRunner::class)
class CreateAccountViewModelTest {

    private lateinit var viewModel: CreateAccountViewModel

    @Mock
    private lateinit var repository: UserAccountsRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var createAccountAccountCall: Call<CreateAccountResponse>

    @Mock
    private lateinit var responseCallback: Callback<CreateAccountResponse>

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

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        viewModel.inputFullName.set("mabu")
        viewModel.onCheckedChanged(null, 2)
        assertEquals(true,  viewModel.validateFields())
        assertEquals(true, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when gender not selected`() {

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        viewModel.inputFullName.set("mabu")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when name not entered`() {

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        viewModel.inputFullName.set("")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when username is not entered`() {

        viewModel.inputUsernameLbl.set("")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        viewModel.inputFullName.set("mabu")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when password is not entered`() {
        viewModel.inputUsernameLbl.set("raj2904201")
        viewModel.inputPasswordLbl.set("")
        viewModel.inputFullName.set("maabu")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should create correct create account payload for entered inputs`() {
        val username = "raj-bande1"
        val password = "Vik2704"
        val fullName = "Maabu"
        val provider = "hegde"
        viewModel.usernameProviderLbl.set(provider)
        viewModel.inputUsernameLbl.set(username)
        viewModel.inputPasswordLbl.set(password)
        viewModel.inputFullName.set(fullName)
        viewModel.selectedYoB(1977)
        viewModel.onCheckedChanged(null, 2)
        val createAccountRequest = viewModel.getCreateAccountPayload()
        assertEquals(username + provider, createAccountRequest.userName)
        assertEquals(password, createAccountRequest.password)
        assertEquals(fullName, createAccountRequest.name)
        assertEquals("O", createAccountRequest.gender)
        assertEquals(1977, createAccountRequest.yearOfBirth)
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
    fun `should create correct create account payload for entered inputs with ayushmanBharatID`() {
        val username = "raj-bande1"
        val password = "Vik2704"
        val fullName = "Maabu"
        val provider = "hegde"
        val bharatId = "PAYUSh123"
        viewModel.usernameProviderLbl.set(provider)
        viewModel.inputUsernameLbl.set(username)
        viewModel.inputPasswordLbl.set(password)
        viewModel.inputFullName.set(fullName)
        viewModel.inputAyushmanIdLbl.set(bharatId)
        viewModel.selectedYoB(1977)
        viewModel.onCheckedChanged(null, 2)
        val createAccountRequest = viewModel.getCreateAccountPayload()
        assertEquals(username + provider, createAccountRequest.userName)
        assertEquals(password, createAccountRequest.password)
        assertEquals(fullName, createAccountRequest.name)
        assertEquals("O", createAccountRequest.gender)
        assertEquals(1977, createAccountRequest.yearOfBirth)
    }

    @Test
    fun `test years to populate of list of 120 years`() {

        val yearsToPopulate = viewModel.getYearsToPopulate()
        assertEquals("yyyy",yearsToPopulate[0])
        assertEquals("2020",yearsToPopulate[1])
        assertEquals("1901",yearsToPopulate[120])
    }
}