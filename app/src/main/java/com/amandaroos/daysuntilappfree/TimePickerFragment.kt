package com.amandaroos.daysuntilappfree

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val sharedPref = context?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        var savedDate =
            sharedPref?.getLong(
                getString(R.string.date_key),
                Calendar.getInstance().timeInMillis
            )

        val c = Calendar.getInstance()
        c.timeInMillis = savedDate!!
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val sharedPref = context?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        var savedDate =
            sharedPref?.getLong(
                getString(R.string.date_key),
                Calendar.getInstance().timeInMillis
            )

        val c = Calendar.getInstance()
        c.timeInMillis = savedDate!!
        c.set(Calendar.HOUR, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)

        sharedPref!!.edit().putLong(getString(R.string.date_key), c.timeInMillis).commit()
    }
}