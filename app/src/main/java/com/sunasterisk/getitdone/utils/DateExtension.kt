package com.sunasterisk.getitdone.utils

import com.sunasterisk.getitdone.utils.Constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToString(): String {
    return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(this)
}

fun Date.dateDiff(other: Date): Int {
    val diff: Long = this.time - other.time
    var dayDiff = diff / 1000 / 60 / 60 / 24
    if ((dayDiff == 0L) && (!isTheSameDay(other)))
        dayDiff++
    return dayDiff.toInt()
}

fun Date.isTheSameDay(other: Date): Boolean {
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(other)
}
