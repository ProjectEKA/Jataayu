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
    fun parseDateShouldParseDateWithoutMinutes() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05Z"))
    }

    @Test
    fun parseDateShouldParseDateWithMinutes() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55Z"))
    }

    @Test
    fun parseDateShouldParseDateWithMilliSeconds() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55:33.318Z"))
    }
    @Test
    fun parseDateShouldReturnErrorMessage() {
        assertEquals("unknown", DateTimeUtils.getFormattedDate("2020-01-06"))
    }
}