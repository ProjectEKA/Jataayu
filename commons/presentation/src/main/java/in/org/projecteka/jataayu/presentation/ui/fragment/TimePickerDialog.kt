package `in`.org.projecteka.jataayu.presentation.ui.fragment

import `in`.org.projecteka.jataayu.presentation.callback.DateTimeSelectionCallback
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TimePickerDialog(private val time: String, private val dateTimeSelectionCallback: DateTimeSelectionCallback) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            dateTimeSelectionCallback.onTimeSelected(Pair(hourOfDay, minute))
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dateFormatter = SimpleDateFormat("HH:mm a", Locale.ENGLISH)
            val c = Calendar.getInstance()
            c.time = dateFormatter.parse(time)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            return TimePickerDialog(activity, android.R.style.Theme_Material_Light_Dialog_Alert, this,
                hour, minute, false)
        }
    }