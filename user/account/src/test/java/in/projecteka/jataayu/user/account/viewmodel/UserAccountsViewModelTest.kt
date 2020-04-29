package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class UserAccountsViewModelTest {

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
    private lateinit var getLinkedAccountsCall: Call<LinkedAccountsResponse>

    @Mock
    private lateinit var responseCallback: ResponseCallback

    private lateinit var viewModel: UserAccountsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = UserAccountsViewModel(repository,preferenceRepository,credentialsRepository)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(repository, createAccountAccountCall)
        Mockito.validateMockitoUsage()
    }

    @Test
    fun shouldCreateAccount() {
        val accountDetails = Gson().fromJson<CreateAccountRequest>(TestUtils.readFile("create_account_request.json"))
        val createAccountResponse = Gson().fromJson<CreateAccountResponse>(TestUtils.readFile("create_account_response.json"))

        Mockito.`when`(repository.createAccount(accountDetails)).thenReturn(createAccountAccountCall)
        Mockito.`when`(createAccountAccountCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<CreateAccountResponse>
                callback.onResponse(createAccountAccountCall, Response.success(createAccountResponse))
            }
        viewModel.createAccount(responseCallback, accountDetails)
        Mockito.verify(repository).createAccount(accountDetails)
        Mockito.verify(createAccountAccountCall).enqueue(ArgumentMatchers.any())
        Assert.assertEquals(createAccountResponse, viewModel.createAccountResponse.value)
    }

    @Test
    fun shouldDisplayAccounts(){
        val linkedAccountsResponse = getLinkedAccountsData()

        Mockito.`when`(repository.getUserAccounts()).thenReturn(getLinkedAccountsCall)
        Mockito.`when`(getLinkedAccountsCall.enqueue(ArgumentMatchers.any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<LinkedAccountsResponse>
                callback.onResponse(getLinkedAccountsCall, Response.success(linkedAccountsResponse))
            }
        viewModel.getUserAccounts(responseCallback)
        Mockito.verify(repository).getUserAccounts()
        Mockito.verify(getLinkedAccountsCall).enqueue(ArgumentMatchers.any())
        Assert.assertEquals(linkedAccountsResponse, viewModel.linkedAccountsResponse.value)
    }

    @Test
    fun shouldGetAccountsToDisplay() {
        viewModel.linkedAccountsResponse.value = getLinkedAccountsData()
        val list = listOf<LinkedAccount>(
            LinkedAccount(providerName="Max Health Care", patientReferenceId="5", patientName="Ron Doe",
            childrenViewModels= listOf<LinkedCareContext>(
                LinkedCareContext(referenceNumber="131", display="National Cancer program"),
                LinkedCareContext(referenceNumber="131", display="National Cancer program")),
                childrenResourceId=2131427419, isExpanded=false),

            LinkedAccount(providerName="Infinity Health care & Diagnostics", patientReferenceId="5", patientName="Ron Doe",
                childrenViewModels= listOf<LinkedCareContext>(
                    LinkedCareContext(referenceNumber="131", display="National Cancer program"),
                        LinkedCareContext(referenceNumber="131", display="National Cancer program"),
                        LinkedCareContext(referenceNumber="131", display="National Cancer program")),
                childrenResourceId=2131427419, isExpanded=false))
        assertEquals(list.count(), viewModel.getDisplayAccounts().count())
    }

    @Test
    fun shouldReturnTrueIfUsernameIsValid() {
        assertTrue(viewModel.isValid("raj", CreateAccountFragment.usernameCriteria))
        assertTrue(viewModel.isValid("rajkiran.bande", CreateAccountFragment.usernameCriteria))
        assertTrue(viewModel.isValid("rajkiran20", CreateAccountFragment.usernameCriteria))
        assertTrue(viewModel.isValid("1raj-bande", CreateAccountFragment.usernameCriteria))
    }

    @Test
    fun shouldReturnFalseIfPasswordIsNotValid() {
        assertFalse(viewModel.isValid("1raj_bande", CreateAccountFragment.usernameCriteria))
        assertFalse(viewModel.isValid("raj@bande", CreateAccountFragment.usernameCriteria))
        assertFalse(viewModel.isValid("rb", CreateAccountFragment.usernameCriteria))
    }
    @Test
    fun shouldReturnTrueIfPasswordIsValid() {
        assertTrue(viewModel.isValid("@Abcd432", CreateAccountFragment.passwordCriteria))
        //length is more than 30 characters
        assertFalse(viewModel.isValid("Abcd123@%1!\"#\$%&'()*+,-./:;<=>?@", CreateAccountFragment.passwordCriteria))
        assertFalse(viewModel.isValid("Abcd@43", CreateAccountFragment.passwordCriteria))
        assertFalse(viewModel.isValid("Abcd@xyz", CreateAccountFragment.passwordCriteria))
        assertFalse(viewModel.isValid("1111@222", CreateAccountFragment.passwordCriteria))
        assertFalse(viewModel.isValid("Abcd4321", CreateAccountFragment.passwordCriteria))
    }

    @Test
    fun `test for spaces`(){
        assertTrue(viewModel.isValid("Abcd@4321  ", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("  Abcd@4321  ", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("  Abcd@4321", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("  A bcd@4321", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("  Abcd@4 321", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("  Abcd@ 4 321", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("  A b cd@4321", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("Test @123", CreateAccountFragment.passwordCriteria))
        assertTrue(viewModel.isValid("      @aA1", CreateAccountFragment.passwordCriteria))
        assertFalse(viewModel.isValid("                ", CreateAccountFragment.passwordCriteria))
    }

    private fun getLinkedAccountsData(): LinkedAccountsResponse? {
        return Gson().fromJson<LinkedAccountsResponse>(TestUtils.readFile("linked_accounts.json"))
    }
}