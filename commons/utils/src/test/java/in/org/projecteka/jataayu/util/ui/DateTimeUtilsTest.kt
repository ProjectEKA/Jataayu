package `in`.org.projecteka.jataayu.util.ui

import junit.framework.Assert.assertEquals
import org.junit.Test

class DateTimeUtilsTest {
    @Test
    fun shouldReturnFormattedDateInDDMMYYFormat() {
        assertEquals("06/01/20", DateTimeUtils.getFormattedDate("2020-01-06T05:55:33.318Z"))
    }
}