package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.core.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.TestUtils
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
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback

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
        verifyNoMoreInteractions(repository, createAccountAccountCall)
        validateMockitoUsage()
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
    fun `validate user name criteria`() {

        viewModel.inputUsernameLbl.set("raj1")
        viewModel.inputPasswordLbl.set("As1@12345")
        viewModel.confirmationInputPasswordLbl.set("As1@12345")
        viewModel.validateUserName()
        assertTrue(!viewModel.showErrorUserName.get())

    }

    @Test
    fun `validate password if pattern matches`() {

        viewModel.inputPasswordLbl.set("As1@12345")
        viewModel.validatePassword()
        assertTrue(!viewModel.showErrorPassword.get())
    }

    @Test
    fun `invalidate password if pattern does not match`() {

        viewModel.inputPasswordLbl.set("As112345")
        viewModel.validatePassword()
        assertFalse(!viewModel.showErrorPassword.get())
    }

    @Test
    fun `invalidate confirmPassword if does not match with password`() {
        viewModel.inputPasswordLbl.set("As@12345")
        viewModel.confirmationInputPasswordLbl.set("Aa@12345")
        viewModel.validateConfirmPassword()
        assertTrue(viewModel.showErrorConfirmPassword.get())
    }

    @Test
    fun `shows username already exists message on error label`() {
        viewModel.showUserAlreadyExistsError()
        assertEquals(`in`.projecteka.jataayu.user.account.R.string.user_already_exits,  viewModel.usernameErrorLbl.get())
        assertEquals(true, viewModel.showErrorUserName.get())
    }

    @Test
    fun `should save tokens to shared preferences after login success`() {

        val response = Gson()
            .fromJson(TestUtils.readFile("create_account_response.json"), CreateAccountResponse::class.java)
        viewModel.onCreateAccountSuccess(response)
        verify(credentialsRepository, times(1)).accessToken = "${response.tokenType.capitalize()} ${response.accessToken}"
        verify(preferenceRepository, times(1)).isUserLoggedIn = true
        verify(credentialsRepository, times(1)).refreshToken = response.refreshToken
    }
}
