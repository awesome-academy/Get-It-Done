package com.sunasterisk.getitdone.data.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_ID
import com.sunasterisk.getitdone.utils.Constants.EMPTY_STRING
import com.sunasterisk.getitdone.utils.Database
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskList(
    var id: Int = DEFAULT_ID,
    var title: String = EMPTY_STRING
) : Parcelable {

    constructor(cursor: Cursor) : this(
        cursor.getInt(cursor.getColumnIndex(ID)),
        cursor.getString(cursor.getColumnIndex(TITLE))
    )

    fun getContentValues() =
        ContentValues().apply {
            put(TITLE, title)
        }
    
    companion object {
        const val TABLE_NAME = "LIST"

        const val ID = "ID"

        const val TITLE = "TITLE"
    }
}
