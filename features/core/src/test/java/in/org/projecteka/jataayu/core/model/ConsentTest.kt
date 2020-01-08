package `in`.org.projecteka.jataayu.core.model

import TestUtils
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
        assertEquals(consent.getPermissionStartDate(), "06/01/19")
    }

    @Test
    fun shouldReturnPermissionEndDate() {
        assertEquals(consent.getPermissionToDate(), "06/01/20")
    }

}