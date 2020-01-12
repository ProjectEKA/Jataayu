package `in`.org.projecteka.jataayu.util.ui

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        fun getFormattedDate(utcDate: String): String {
            val date = getDate(utcDate)
            val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return outputFormat.format(date!!)
        }

        private fun getDate(utcDate: String): Date? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
            return inputFormat.parse(utcDate)
        }

        fun getRelativeTimeSpan(createdAt: String): String {
            return DateUtils.getRelativeTimeSpanString(getDate(createdAt)!!.time).toString()
        }

        fun getFormattedDateTime(utcDate: String): String {
            val date = getDate(utcDate)
            val outputFormat = SimpleDateFormat("hha, dd/MM/yy", Locale.getDefault())
            return outputFormat.format(date!!)
        }
    }
}