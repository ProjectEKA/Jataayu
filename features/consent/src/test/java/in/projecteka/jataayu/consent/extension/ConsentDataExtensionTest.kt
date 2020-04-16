package `in`.projecteka.jataayu.consent.extension

import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.util.TestUtils
import com.google.gson.Gson
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class ConsentDataExtensionTest {

    private lateinit var consentsListResponse: ConsentsListResponse

    @Before
    fun setUp() {
        consentsListResponse = Gson()
            .fromJson(TestUtils.readFile("consent_list_response.json"), ConsentsListResponse::class.java)
    }

    @Test
    fun `should Sort Consent List By LastUpdatedDate In AscendingOrder`() {
        val sortedList = consentsListResponse.requests.getSortedConsentListByLastUpdatedDate(SortOrder.ASC)
        val first = sortedList.first().getLastUpdated()
        val second = sortedList[1].getLastUpdated()
        val last = sortedList.last().getLastUpdated()
        assertTrue(first!!.before(second) && last!!.after(second))
        assertTrue(consentsListResponse.requests.count() == sortedList.count())
    }

    @Test
    fun `should Sort Consent ListBy LastUpdatedDate In DescendingOrder`() {
        val sortedList = consentsListResponse.requests.getSortedConsentListByLastUpdatedDate(SortOrder.DESC)
        val first = sortedList.first().getLastUpdated()
        val second = sortedList[1].getLastUpdated()
        val last = sortedList.last().getLastUpdated()
        assertTrue(first!!.after(second) && second!!.after(last))
        assertTrue(consentsListResponse.requests.count() == sortedList.count())
    }
}