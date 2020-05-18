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
        viewModel = CreateAccountViewModel(repository, preferenceRepository, credentialsRepository)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repository, createAccountAccountCall)
        Mockito.validateMockitoUsage()
    }

    @Test
    fun `should set error flag when username have empty`() {
        viewModel.inputUsernameLbl.set("")
        viewModel.validateUserName()
        assertFalse(!viewModel.showErrorUserName.get())
    }

    @Test
    fun `should set error flag when username have only have just space`() {
        viewModel.inputUsernameLbl.set(" ")
        viewModel.validateUserName()
        assertEquals(true, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should set error flag when username have only have 2 character`() {
        viewModel.inputUsernameLbl.set("ra")
        viewModel.validateUserName()
        assertEquals(true, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should set error flag when username have special character`() {
        viewModel.inputUsernameLbl.set("raj@!#$%^&*()")
        viewModel.validateUserName()
        assertEquals(true, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should not set error flag when username have minimum 3 characters`() {
        viewModel.inputUsernameLbl.set("raj")
        viewModel.validateUserName()
        assertEquals(false, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should not set error flag when username contains number`() {
        viewModel.inputUsernameLbl.set("raj3")
        viewModel.validateUserName()
        assertEquals(false, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should not set error flag when username contains dot and hyphen`() {
        viewModel.inputUsernameLbl.set("raj3.-")
        viewModel.validateUserName()
        assertEquals(false, viewModel.showErrorUserName.get())
    }

    @Test
    fun `username must be minimum 3 characters long, one uppercase one lower case, one number and only dot and hypen allowed`() {
        viewModel.inputUsernameLbl.set("raj3.-A9")
        viewModel.validateUserName()
        assertEquals(false, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should set error flag when password is less than 8 characters`() {
        viewModel.inputPasswordLbl.set(" ab")
        viewModel.validatePassword()
        assertEquals(true, viewModel.showErrorPassword.get())
    }

    @Test
    fun `should set error flag when password does not contains at least one uppercase`() {
        viewModel.inputPasswordLbl.set(" abcdefg1")
        viewModel.validatePassword()
        assertEquals(true, viewModel.showErrorPassword.get())
    }

    @Test
    fun `should set error flag when password length is more than 30 characters`() {
        viewModel.inputPasswordLbl.set("Abcd123@%1!\\\"#\\\$%&'()*+,-./:;<=>?@")
        viewModel.validatePassword()
        assertEquals(true, viewModel.showErrorPassword.get())
    }

    @Test
    fun `should set error flag when password does not contains special characters`() {
        viewModel.inputPasswordLbl.set("Abcd12356")
        viewModel.validatePassword()
        assertEquals(true, viewModel.showErrorPassword.get())
    }

    @Test
    fun `should set error flag when password does not contains number`() {
        viewModel.inputPasswordLbl.set("Abcdefghjkl@")
        viewModel.validatePassword()
        assertEquals(true, viewModel.showErrorPassword.get())
    }

    @Test
    fun `password must be minimum 8 characters should contain at least one uppercase, one lowercase, special character and spaces allowed`() {
        viewModel.inputPasswordLbl.set("Abcd 12356@")
        viewModel.validatePassword()
        assertEquals(false, viewModel.showErrorPassword.get())
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
    }
}