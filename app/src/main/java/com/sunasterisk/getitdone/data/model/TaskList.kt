package com.sunasterisk.getitdone.data.model

import android.os.Parcelable
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_ID
import com.sunasterisk.getitdone.utils.Constants.EMPTY_STRING
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskList(
    var id: Int = DEFAULT_ID,
    var title: String = EMPTY_STRING
) : Parcelable
