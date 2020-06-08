package `in`.projecteka.jataayu.util.ui

import junit.framework.Assert.assertEquals
import org.junit.Test

class DateTimeUtilsTest {
    @Test
    fun shouldReturnFormattedDateInDDMMYYFormat() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55:33.318Z"))
    }

    @Test
    fun parseDateShouldParseDateWithoutSeconds() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55Z"))
    }

    @Test
    fun parseDateShouldParseDateWithSeconds() {
       assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55:33Z"))
    }

    @Test
    fun parseDateShouldParseDateWithMilliSeconds() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55:33.318Z"))
    }
    @Test
    fun parseDateShouldParseDateWith() {
        assertEquals("Unable to parse Date", DateTimeUtils.getFormattedDate("2020-01-06T05"))
    }
}