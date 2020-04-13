package `in`.projecteka.jataayu.presentation.callback

import java.util.*

interface DateTimeSelectionCallback {
    fun onDateSelected(datePickerId: Int, date: Date)
    fun onTimeSelected(timePair: Pair<Int, Int>)
}