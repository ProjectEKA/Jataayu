package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
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
import retrofit2.Callback

@RunWith(MockitoJUnitRunner::class)
class RaedValuesToRecoveerCmidFragmentViewModelTest {

    private lateinit var viewModel: ReadValuesFragmentViewModel

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
        viewModel = ReadValuesFragmentViewModel(repository, preferenceRepository)
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
    fun `should not set error flag when name is not empty`() {
        viewModel.inputFullName.set("Anu")
        viewModel.validateName()
        assertEquals(false, viewModel.showErrorName.get())
    }

    @Test
    fun `should set error flag when mobile is empty`() {
        viewModel.inputMobileNumber.set("")
        viewModel.validateMobileNumber()
        assertEquals(true, viewModel.showErrorMobile.get())
    }

    @Test
    fun `should not set error flag when mobile is not empty`() {
        viewModel.inputMobileNumber.set("1234567890")
        viewModel.validateMobileNumber()
        assertEquals(false, viewModel.showErrorMobile.get())
    }

    @Test
    fun `should set error flag when gender is not checked`() {
        viewModel.onCheckedChanged(null, -1)
        assertEquals(true, viewModel.showErrorGender.get())
    }

    @Test
    fun `should enable submit button when all the required fields entered`() {
        viewModel.inputFullName.set("Anu")
        viewModel.inputMobileNumber.set("1234567890")
        viewModel.onCheckedChanged(null, 2)
        assertEquals(true,  viewModel.validateFields())
        assertEquals(true, viewModel.submitEnabled.get())
    }


    @Test
    fun `should disable submit button when gender not selected`() {
        viewModel.inputFullName.set("Anu")
        viewModel.inputMobileNumber.set("1234567890")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when name not entered`() {
        viewModel.inputMobileNumber.set("1234567890")
        viewModel.onCheckedChanged(null, 2)
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when mobile number not entered`() {
        viewModel.inputFullName.set("")
        viewModel.onCheckedChanged(null, 2)
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should create correct recover cmid payload for entered inputs`() {
        val fullName = "Anu"
        val mobile = "9876543210"
        val ayushmanId = "PAyush123"
        viewModel.inputFullName.set(fullName)
        viewModel.inputMobileNumber.set(mobile)
        viewModel.inputAyushmanIdLbl.set(ayushmanId)
        viewModel.selectedYoB(1977)
        viewModel.onCheckedChanged(null, 2)

        val recoverCmidRequest = viewModel.getRecoverCmidPayload()
        assertEquals(fullName, recoverCmidRequest.name)
        assertEquals(viewModel.countryCode.get() + mobile, recoverCmidRequest.verifiedIdentifiers?.get(0)?.value)
        assertEquals(ayushmanId.toUpperCase(), recoverCmidRequest.unverifiedIdentifiers?.get(0)?.value)
        assertEquals("O", recoverCmidRequest.gender)
        assertEquals(1977, recoverCmidRequest.yearOfBirth)
    }

    /*











    @Test
    fun `should create an account`() {
        val accountDetails = Gson().fromJson<CreateAccountRequest>(TestUtils.readFile("create_account_request.json"))
        viewModel.usernameProviderLbl.set("@ncg")
        viewModel.inputUsernameLbl.set(accountDetails.userName)
        viewModel.inputPasswordLbl.set(accountDetails.password)
        viewModel.inputFullName.set(accountDetails.name)
        viewModel.selectedYoB(accountDetails.yearOfBirth!!)
        viewModel.onCheckedChanged(null, 2)

        val createAccountResponse = Gson().fromJson<CreateAccountResponse>(TestUtils.readFile("create_account_response.json"))
        val payload = viewModel.getCreateAccountPayload()
        Mockito.`when`(repository.createAccount(payload)).thenReturn(createAccountAccountCall)
        Mockito.`when`(createAccountAccountCall.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
                callback.onResponse(createAccountAccountCall, Response.success(createAccountResponse))
            }
        viewModel.createAccountResponse.observeForever {
            when(it) {
                is Success -> {
                    val response = viewModel.createAccountResponse.value as Success<CreateAccountResponse>
                    assertEquals(createAccountResponse, response.data)
                }
            }
        }
        viewModel.createAccount()
        Mockito.verify(repository).createAccount(payload)
        Mockito.verify(createAccountAccountCall).enqueue(any())
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
    }*/
}