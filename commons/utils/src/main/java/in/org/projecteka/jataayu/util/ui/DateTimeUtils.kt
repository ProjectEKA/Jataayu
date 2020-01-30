package `in`.org.projecteka.jataayu.util.ui

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        private const val DATE_FORMAT_DD_MM_YY = "dd MMM, yyyy"
        private const val DATE_TIME_FORMAT_DD_MMM_YYYY_HH_A = "hh a, $DATE_FORMAT_DD_MM_YY"
        private const val TIME_FORMAT_HH_MM_A = "hh:mm a"
        fun getFormattedDate(utcDate: String): String {
            val date = getDate(utcDate)
            val outputFormat = SimpleDateFormat(DATE_FORMAT_DD_MM_YY, Locale.getDefault())
            return outputFormat.format(date!!)
        }

        fun getDate(utcDate: String): Date? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.UK)
            inputFormat.timeZone = TimeZone.getTimeZone("GMT")
            return inputFormat.parse(utcDate)
        }

        fun getRelativeTimeSpan(createdAt: String): String {
            return DateUtils.getRelativeTimeSpanString(getDate(createdAt)!!.time).toString()
        }

        fun getFormattedDateTime(utcDate: String): String {
            val date = getDate(utcDate)
            val outputFormat = SimpleDateFormat(DATE_TIME_FORMAT_DD_MMM_YYYY_HH_A, Locale.UK)
            return outputFormat.format(date!!)
        }

        fun getFormattedTime(utcDate: String): String {
            val date = getDate(utcDate)
            val outputFormat = SimpleDateFormat(TIME_FORMAT_HH_MM_A, Locale.UK)
            return outputFormat.format(date!!)
        }

        fun getUtcDate(date: Date): String {
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.UK)
            outputFormat.timeZone = TimeZone.getTimeZone("GMT")
            return outputFormat.format(date)
        }
    }
}