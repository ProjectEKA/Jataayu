package `in`.projecteka.jataayu.presentation.ui.fragment

import `in`.projecteka.jataayu.presentation.callback.DateTimeSelectionCallback
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialog(
    private val resId: Int,
    private val selectedDateMillis: Long,
    private val minDate: Long = UNDEFINED_DATE,
    private val maxDate: Long = UNDEFINED_DATE,
    private val dateTimeSelectionCallback: DateTimeSelectionCallback
) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    companion object {
        const val UNDEFINED_DATE = -1L
    }

    constructor(resId: Int, selectedDateMillis: Long, dateTimeSelectionCallback: DateTimeSelectionCallback) : this(
        resId,
        selectedDateMillis,
        selectedDateMillis,
        UNDEFINED_DATE,
        dateTimeSelectionCallback
    )

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        dateTimeSelectionCallback.onDateSelected(resId, calendar.time)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.time = Date(selectedDateMillis)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(context!!, android.R.style.Theme_Material_Light_Dialog_MinWidth, this, year, month, day)
        if (minDate != UNDEFINED_DATE) datePickerDialog.datePicker.minDate = minDate
        if (maxDate != UNDEFINED_DATE) datePickerDialog.datePicker.maxDate = maxDate
        return datePickerDialog
    }
}