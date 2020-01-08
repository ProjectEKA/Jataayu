package `in`.org.projecteka.jataayu.core.viewmodel

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.TestUtils.Companion.readFile
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import android.content.res.Resources
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ConsentViewModelTest{
    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    private lateinit var resources: Resources

    private lateinit var consentViewModel: ConsentViewModel
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        consentViewModel = ConsentViewModel(repository)
        val consentsListResponse = Gson().fromJson(readFile("consent_list_response.json"), ConsentsListResponse::class.java)
        consentViewModel.requests = consentsListResponse.requests
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
        list.add("All requests (2)")
        list.add("Requested (1)")
        list.add("Expired (1)")
        return list
    }
}