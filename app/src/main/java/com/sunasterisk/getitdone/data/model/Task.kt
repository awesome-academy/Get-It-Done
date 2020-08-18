package com.sunasterisk.getitdone.data.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import com.sunasterisk.getitdone.utils.Constants.DAY_FORMAT
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_COLOR
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_ID
import com.sunasterisk.getitdone.utils.Constants.EMPTY_STRING
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import com.sunasterisk.getitdone.utils.Constants.TRUE
import com.sunasterisk.getitdone.utils.formatToString
import java.util.*

data class Task(
    var id: Int = DEFAULT_ID,
    var listId: Int = DEFAULT_ID,
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var timeReminder: String = EMPTY_STRING,
    var isImportant: Boolean = false,
    var inMyDay: String = EMPTY_STRING,
    var color: String = DEFAULT_COLOR,
    var status: String = STATUS_NOT_COMPLETE,
    var timeCreated: String = EMPTY_STRING
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: EMPTY_STRING,
        parcel.readString() ?: EMPTY_STRING,
        parcel.readString() ?: EMPTY_STRING,
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: EMPTY_STRING,
        parcel.readString() ?: EMPTY_STRING,
        parcel.readString() ?: EMPTY_STRING,
        parcel.readString() ?: EMPTY_STRING
    )

    constructor(cursor: Cursor) : this(
        cursor.getInt(cursor.getColumnIndex(ID)),
        cursor.getInt(cursor.getColumnIndex(LIST_ID)),
        cursor.getString(cursor.getColumnIndex(TITLE)),
        cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
        cursor.getString(cursor.getColumnIndex(TIME_REMINDER)),
        cursor.getInt(cursor.getColumnIndex(IS_IMPORTANT)) == TRUE,
        cursor.getString(cursor.getColumnIndex(IN_MY_DAY)),
        cursor.getString(cursor.getColumnIndex(COLOR)),
        cursor.getString(cursor.getColumnIndex(STATUS)),
        cursor.getString(cursor.getColumnIndex(TIME_CREATED))
    )

    fun getContentValues() =
        ContentValues().apply {
            put(LIST_ID, listId)
            put(TITLE, title)
            put(DESCRIPTION, description)
            put(TIME_REMINDER, timeReminder)
            put(IS_IMPORTANT, isImportant)
            put(IN_MY_DAY, inMyDay)
            put(COLOR, color)
            put(STATUS, status)
            put(TIME_CREATED, timeCreated)
        }

    fun isInMyDay() = inMyDay == Date().formatToString(DAY_FORMAT)

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }

        const val TABLE_NAME = "TASK"

        const val ID = "ID"

        const val LIST_ID = "LIST_ID"

        const val TITLE = "TITLE"

        const val DESCRIPTION = "DESCRIPTION"

        const val TIME_REMINDER = "TIME_REMINDER"

        const val IS_IMPORTANT = "IS_IMPORTANT"

        const val IN_MY_DAY = "IN_MY_DAY"

        const val COLOR = "COLOR"

        const val STATUS = "STATUS"

        const val TIME_CREATED = "TIME_CREATED"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(listId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(timeReminder)
        parcel.writeByte(if (isImportant) 1 else 0)
        parcel.writeString(inMyDay)
        parcel.writeString(color)
        parcel.writeString(status)
        parcel.writeString(timeCreated)
    }

    override fun describeContents(): Int {
        return 0
    }
}
