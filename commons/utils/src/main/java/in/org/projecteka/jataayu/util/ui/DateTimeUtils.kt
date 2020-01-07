package `in`.org.projecteka.jataayu.util.ui

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        public fun getFormattedDate(utcDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
            val date = inputFormat.parse(utcDate)
            val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return outputFormat.format(date!!)
        }
    }
}