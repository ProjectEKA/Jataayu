package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.util.TestUtils
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConsentTest {
    private lateinit var consent: Consent
    @Before
    fun setUp() {
        consent = Gson().fromJson<Consent>(TestUtils.readFile("consent.json"), Consent::class.java)!!
    }

    @Test
    fun shouldReturnPermissionStartDate() {
        assertEquals("16 Jan, 2020", consent.getPermissionStartDate())
    }

    @Test
    fun shouldReturnPermissionEndDate() {
        assertEquals("16 Feb, 2020", consent.getPermissionEndDate())
    }

}