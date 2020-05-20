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
class ConfirmAccountViewModelTest {

    private lateinit var viewModel: ConfirmAccountViewModel

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
        viewModel = ConfirmAccountViewModel(repository, preferenceRepository, credentialsRepository)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repository, createAccountAccountCall)
        Mockito.validateMockitoUsage()
    }

    @Test
    fun `should enable submit button when all the required fields entered`() {

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.confirmationInputPasswordLbl.set("Raj1234@13")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        assertEquals(true,  viewModel.validateFields())
        assertEquals(true, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when gender not selected`() {

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when name not entered`() {

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when username is not entered`() {

        viewModel.inputUsernameLbl.set("")
        viewModel.inputPasswordLbl.set("Raj1234@13")
        assertEquals(false,  viewModel.validateFields())
        assertEquals(false, viewModel.submitEnabled.get())
    }

    @Test
    fun `should disable submit button when password is not entered`() {
        viewModel.inputUsernameLbl.set("raj2904201")
        viewModel.inputPasswordLbl.set("")
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
        viewModel.onCheckedChanged(null, 2)
        val createAccountRequest = viewModel.getCreateAccountPayload()
        assertEquals(username + provider, createAccountRequest.userName)
        assertEquals(password, createAccountRequest.password)
        assertEquals(fullName, createAccountRequest.name)
        assertEquals("O", createAccountRequest.gender)
    }




    @Test
    fun `should create an account`() {
        val accountDetails = Gson().fromJson<CreateAccountRequest>(TestUtils.readFile("create_account_request.json"))
        viewModel.usernameProviderLbl.set("@ncg")
        viewModel.inputUsernameLbl.set(accountDetails.userName)
        viewModel.inputPasswordLbl.set(accountDetails.password)
        viewModel.inputFullName.set(accountDetails.name)
        //viewModel.selectedYoB(accountDetails.yearOfBirth!!)
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




}
