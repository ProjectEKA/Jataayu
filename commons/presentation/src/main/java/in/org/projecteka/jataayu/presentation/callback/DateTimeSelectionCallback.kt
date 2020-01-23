package `in`.org.projecteka.jataayu.presentation.callback

import androidx.annotation.IdRes
import java.util.*

interface DateTimeSelectionCallback {
    fun onDateSelected(@IdRes datePickerId: Int, date: Date)
    fun onTimeSelected(timePair: Pair<Int, Int>)
}