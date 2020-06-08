package `in`.projecteka.jataayu.util.ui

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        private const val DATE_FORMAT_DD_MM_YY = "dd MMM, yyyy"
        private const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
        private const val DATE_TIME_FORMAT_DD_MMM_YYYY_HH_A = "hh a, $DATE_FORMAT_DD_MM_YY"
        private const val TIME_FORMAT_HH_MM_A = "hh:mm a"
        fun getFormattedDate(utcDate: String): String {
            return getFormattedDateTime(utcDate,DATE_FORMAT_DD_MM_YY)
        }

        fun getDate(utcDate: String): Date? {
            var parsedDate: Date? = null
            val dateFormats = Arrays.asList(
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm"
            )
            for (dateFormat in dateFormats) {
                val inputFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                parsedDate = try {
                    inputFormat.parse(utcDate)
                } catch (e: ParseException) {
                    null
                }
            }
            return parsedDate
        }

        fun getRelativeTimeSpan(createdAt: String): String {
            return DateUtils.getRelativeTimeSpanString(getDate(createdAt)!!.time).toString()
        }

        fun getFormattedDateTime(utcDate: String): String {
            return getFormattedDateTime(utcDate,DATE_TIME_FORMAT_DD_MMM_YYYY_HH_A)
        }

        fun getFormattedTime(utcDate: String): String {
            return getFormattedDateTime(utcDate,TIME_FORMAT_HH_MM_A)
        }

        private fun getFormattedDateTime(utcDate: String, format: String) :String{
            val date = getDate(utcDate)
            val outputFormat = SimpleDateFormat(format, Locale.getDefault())
            val parseDate= date?.let {
                outputFormat.format(it)
            }

            return parseDate ?: "Unable to parse Date"
        }

        fun getUtcDate(date: Date): String {
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            return outputFormat.format(date)
        }

        fun isDateExpired(date: String): Boolean {
            val calendarExpiry = Calendar.getInstance()
            val calendarToday = Calendar.getInstance()
            calendarExpiry.timeZone = TimeZone.getTimeZone("UTC")
            calendarExpiry.time = getDate(date)
            return (calendarExpiry.time).before(calendarToday.time)
        }
    }
}