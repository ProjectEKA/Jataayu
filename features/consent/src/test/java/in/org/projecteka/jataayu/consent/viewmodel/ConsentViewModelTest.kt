package `in`.org.projecteka.jataayu.core.viewmodel

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.util.TestUtils
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ConsentViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var progressDialogCallback: ProgressDialogCallback

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var call: Call<ConsentsListResponse>

    private lateinit var consentViewModel: ConsentViewModel

    private lateinit var consentsListResponse: ConsentsListResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        consentViewModel = ConsentViewModel(repository)

        consentsListResponse =
            Gson().fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)
        `when`(repository.getConsents()).thenReturn(call)
        `when`(call.enqueue(any()))
            .then { invocation ->
                val callback = invocation.arguments[0] as Callback<ConsentsListResponse>
                callback.onResponse(call, Response.success(consentsListResponse))
            }

        consentViewModel.getConsents(progressDialogCallback)
    }

    @Test
    fun shouldPopulateFilterItems() {
        `when`(resources.getString(R.string.status_all_requests)).thenReturn("All requests (%d)")
        `when`(resources.getString(R.string.status_active_requests)).thenReturn("Requested (%d)")
        `when`(resources.getString(R.string.status_expired_requests)).thenReturn("Expired (%d)")
        val populatedFilterItems = consentViewModel.populateFilterItems(resources)

        assertEquals(dummyList(), populatedFilterItems)
    }

    private fun dummyList(): ArrayList<String> {
        val list = ArrayList<String>(3)
        list.add("All requests (14)")
        list.add("Requested (12)")
        list.add("Expired (2)")
        return list
    }

    @Test
    fun shouldReturnFalseIfRequestsNotAvailable() {
        assertTrue(consentViewModel.isRequestAvailable())
    }

    @Test
    fun shouldFetchConsents(){
        verify(repository).getConsents()
        verify(call).enqueue(any())
        assertEquals(consentsListResponse, consentViewModel.consentsListResponse.value)
    }
}