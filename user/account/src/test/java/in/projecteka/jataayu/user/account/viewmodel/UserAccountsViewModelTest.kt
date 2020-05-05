package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.MyProfile
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.user.account.remote.UserAccountApis
import `in`.projecteka.jataayu.user.account.repository.UserAccountsRepository
import `in`.projecteka.jataayu.util.TestUtils
import `in`.projecteka.jataayu.util.extension.fromJson
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
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


@RunWith(MockitoJUnitRunner::class)
class UserAccountsViewModelTest {

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
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var userAccountApis: UserAccountApis

    @Mock
    private lateinit var profileObserver: Observer<PayloadResource<MyProfile>>

    @Mock
    private lateinit var userAccountListObserver: Observer<PayloadResource<LinkedAccountsResponse>>

    @Mock
    private lateinit var logoutCall: Call<Void>

    private lateinit var viewModel: UserAccountsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
        lifecycleRegistry.handleLifecycleEvent(ON_CREATE)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycleRegistry)
        viewModel = UserAccountsViewModel(repository,preferenceRepository,credentialsRepository)
    }


    @After
    fun tearDown() {
        verifyNoMoreInteractions(repository, profileResponseCall, linkedAcccoutCall, lifecycleOwner, profileObserver, logoutCall)
        validateMockitoUsage()
    }

    @Test
    fun `should display link accounts list and user profile details `() {
        val myProfile = getMyProfileResponse()
        val linkedAccountsResponse = getLinkedAccountsResponse()



        viewModel.userProfileResponse.addSource(viewModel.getUserAccounts(), userAccountListObserver)
        viewModel.userProfileResponse.addSource(viewModel.getMyProfile(), profileObserver)
        viewModel.updateProfile.value  = myProfile
        verify(profileObserver).onChanged(Success(myProfile))
    }

    @Test
    fun `should create linked accounts by links response `() {
        val linkedAccountsResponse = getLinkedAccountsResponse()
        viewModel.updateDisplayAccounts(linkedAccountsResponse!!.linkedPatient.links)

        assertEquals(2, viewModel.updateLinks.value?.count())
        assertEquals(2, viewModel.linksSize.get())
    }


    @Test
    fun `should clear all shared preferences`() {
        val myProfile = getLinkedAccountsResponse()
        val linkedAccountsResponse = getMyProfileResponse()

        val profileLiveData = PayloadLiveData<MyProfile>()
        val linkedAccountsLiveData = PayloadLiveData<LinkedAccountsResponse>()

        `when`(repository.getMyProfile()).thenReturn(profileLiveData)
        `when`(profileResponseCall.enqueue(any())).then {
            (it.arguments[0] as Callback<MyProfile>).apply {
                onResponse(profileResponseCall, Response.success(myProfile))
            }
        }

        `when`(repository.getUserAccounts()).thenReturn(linkedAccountsLiveData)
        `when`(linkedAcccoutCall.enqueue(any())).then {
            (it.arguments[0] as Callback<LinkedAccountsResponse>).apply {
                onResponse(linkedAcccoutCall, Response.success(linkedAccountsResponse))
            }
        }

        viewModel.updateProfile.observeForever {  }

        viewModel.userProfileResponse.observeForever {
            when(it) {
                is Success -> {
                    assertEquals(linkedAccountsResponse, viewModel.updateProfile.value)
                }
            }

        }
        viewModel.fetchAll()
        verify(repository).getMyProfile()
        verify(repository).getUserAccounts()
    }


    private fun getLinkedAccountsResponse(): LinkedAccountsResponse? {
        return Gson().fromJson<LinkedAccountsResponse>(TestUtils.readFile("linked_accounts.json"))
    }

    private fun getMyProfileResponse(): MyProfile? {
        return Gson().fromJson<MyProfile>(TestUtils.readFile("my_profile.json"))
    }
}

