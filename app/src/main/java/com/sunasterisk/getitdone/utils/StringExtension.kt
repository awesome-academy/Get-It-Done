package com.sunasterisk.getitdone.utils

import com.sunasterisk.getitdone.utils.Constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date? {
    return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(this)
}
