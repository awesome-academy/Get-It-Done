package com.sunasterisk.getitdone.data.model

import android.os.Parcelable
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_COLOR
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_ID
import com.sunasterisk.getitdone.utils.Constants.EMPTY_STRING
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var id: Int = DEFAULT_ID,
    var listId: Int = DEFAULT_ID,
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var timeReminder: String = EMPTY_STRING,
    var isImportant: Boolean = false,
    var isInMyDay: Boolean = false,
    var color: String = DEFAULT_COLOR,
    var status: Int = STATUS_NOT_COMPLETE,
    var timeCreated: String = EMPTY_STRING
) : Parcelable
