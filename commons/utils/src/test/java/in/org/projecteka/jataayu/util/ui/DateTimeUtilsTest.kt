package `in`.org.projecteka.jataayu.util.ui

import junit.framework.Assert.assertEquals
import org.junit.Test

class DateTimeUtilsTest {
    @Test
    fun shouldReturnFormattedDateInDDMMYYFormat() {
        assertEquals("06 Jan, 2020", DateTimeUtils.getFormattedDate("2020-01-06T05:55:33.318Z"))
    }

    @Test
    fun shouldReturnFormattedDateTimeInDDMMYYFormat() {
        assertEquals("11 AM, 06 Jan, 2020", DateTimeUtils.getFormattedDateTime("2020-01-06T05:55:33.318Z"))
    }

    @Test
    fun shouldReturnFormattedTimeInDDMMYYFormat() {
        assertEquals("11:25 AM", DateTimeUtils.getFormattedTime("2020-01-06T05:55:33.318Z"))
    }
}