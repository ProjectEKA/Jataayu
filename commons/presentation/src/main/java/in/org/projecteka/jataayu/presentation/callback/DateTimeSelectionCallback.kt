package `in`.org.projecteka.jataayu.presentation.callback

import androidx.annotation.IdRes
import androidx.annotation.IntRange
import java.util.*

interface DateTimeSelectionCallback {
    fun onDateSelected(@IdRes datePickerId: Int, date: Date)
    fun onTimeSelected(date: Pair<Int, Int>)
}