package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.MyProfile
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
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
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner.Silent::class)
class ProfileFragmentViewModelTest {

    @Mock
    private lateinit var repository: UserAccountsRepository

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @Mock
    private lateinit var credentialsRepository: CredentialsRepository

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    @Mock
    private lateinit var linkedAcccoutCall: Call<LinkedAccountsResponse>

    @Mock
    private lateinit var profileResponseCall: Call<MyProfile>


    @Mock
    private lateinit var logoutCall: Call<Void>

    private lateinit var viewModel: ProfileFragmentViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProfileFragmentViewModel(repository,preferenceRepository,credentialsRepository)
    }


    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository, profileResponseCall, linkedAcccoutCall, logoutCall)
        validateMockitoUsage()
    }

    @Test
    fun `should logout user`() {

        val refreshToken = "abc"
        `when`(credentialsRepository.refreshToken).thenReturn(refreshToken)
        `when`(repository.logout(refreshToken)).thenReturn(logoutCall).then {
                invocation ->
            val callback = invocation.arguments[0] as Callback<Void>
            callback.onResponse(logoutCall, Response.success(null))
        }
        viewModel.logout()
        verify(repository).logout(refreshToken)
        verify(logoutCall).enqueue(any())
    }

    @Test
    fun `should clear all shared preferences`() {

        viewModel.clearSharedPreferences()
        verify(credentialsRepository, times(1)).reset()
        verify(preferenceRepository, times(1)).resetPreferences()
    }

    @Test
    fun `should set consent pin creation status if pin has created`(){
        `when`(preferenceRepository.pinCreated).thenReturn(true)
        viewModel.setConsentPinStatus()
        assertFalse(viewModel.showPinNotCreated.get())
        assertEquals(R.string.edit, viewModel.pinCreateOrEdit.get())
    }

    @Test
    fun `should set consent pin creation status if pin not created`(){
        `when`(preferenceRepository.pinCreated).thenReturn(false)
        viewModel.setConsentPinStatus()
        assertTrue(viewModel.showPinNotCreated.get())
        assertEquals(R.string.create, viewModel.pinCreateOrEdit.get())
    }

    @Test
    fun `should redirect to eedit consent pin screen`(){
        viewModel.redirectToEditConsentPin()
        assertEquals(ProfileFragmentViewModel.RedirectTo.CONSENT_PIN, viewModel.redirectTo.value)
    }


    private fun getLinkedAccountsResponse(): LinkedAccountsResponse? {
        return Gson().fromJson<LinkedAccountsResponse>(TestUtils.readFile("linked_accounts.json"))
    }

    private fun getMyProfileResponse(): MyProfile? {
        return Gson().fromJson<MyProfile>(TestUtils.readFile("my_profile.json"))
    }
}

