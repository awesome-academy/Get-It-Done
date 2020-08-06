package com.sunasterisk.getitdone.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import java.util.*

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showDateTimePicker(calendar: Calendar, callback: (Calendar) -> Unit) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val pickedTime = Calendar.getInstance()
    DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, _year, _month, _dayOfMonth ->
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            pickedTime.set(_year, _month, _dayOfMonth, hourOfDay, minute)
            callback(pickedTime)
        }, hour, minute, true).show()
    }, year, month, day).show()
}
