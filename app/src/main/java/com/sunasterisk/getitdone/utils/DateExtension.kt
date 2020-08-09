package com.sunasterisk.getitdone.utils

import com.sunasterisk.getitdone.utils.Constants.DATE_FORMAT
import com.sunasterisk.getitdone.utils.Constants.DAY_FORMAT
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToString(format: String = DATE_FORMAT): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}

fun Date.dateDiff(other: Date): Int {
    val diff: Long = this.time - other.time
    var dayDiff = diff / 1000 / 60 / 60 / 24
    if ((dayDiff == 0L) && (!isTheSameDay(other)))
        dayDiff++
    return dayDiff.toInt()
}

fun Date.isTheSameDay(other: Date): Boolean {
    val sdf = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())
    return sdf.format(this) == sdf.format(other)
}
