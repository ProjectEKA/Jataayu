package `in`.projecteka.jataayu.consent.helper

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.viewmodel.RequestedConsentViewModel
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.util.TestUtils
import com.google.gson.Gson
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
internal class ConsentDataHelperTest {

    private lateinit var consentsListResponse: ConsentsListResponse

    @Before
    fun setUp() {
        consentsListResponse = Gson()
            .fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)
    }

    @Test
    fun shouldSortConsentListByLastUpdatedDateInAscendingOrder() {
        val sortedList = ConsentDataHelper.sortConsentListByLastUpdatedDate(consentsListResponse.requests, ConsentDataHelper.SortOrder.ASC)
        val first = sortedList.first().getLastUpdated()
        val second = sortedList[1].getLastUpdated()
        val last = sortedList.last().getLastUpdated()
        assertTrue(first!!.before(second) && last!!.after(second))
        assertTrue(consentsListResponse.requests.count() == sortedList.count())
    }

    fun shouldSortConsentListByLastUpdatedDateInDescendingOrder() {
        val sortedList = ConsentDataHelper.sortConsentListByLastUpdatedDate(consentsListResponse.requests, ConsentDataHelper.SortOrder.DESC)
        val first = sortedList.first().getLastUpdated()
        val second = sortedList[1].getLastUpdated()
        val last = sortedList.last().getLastUpdated()
        assertTrue(first!!.after(second) && second!!.after(last))
        assertTrue(consentsListResponse.requests.count() == sortedList.count())
    }
}