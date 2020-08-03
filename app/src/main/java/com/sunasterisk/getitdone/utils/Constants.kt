package com.sunasterisk.getitdone.utils

import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList

object Constants {
    const val DEFAULT_ID = -1
    const val EMPTY_STRING = ""
    const val STATUS_NOT_COMPLETE = "NOT COMPLETED YET"
    const val STATUS_COMPLETED = "COMPLETED"

    const val DEFAULT_COLOR = "#607D8B"
    const val COLOR_GREEN = "#4CAF50"
    const val COLOR_CYAN = "#00BCD4"
    const val COLOR_LIME = "#CDDC39"
    const val COLOR_ORANGE = "#FFC107"
    const val COLOR_RED = "#F44336"
    const val COLOR_PURPLE = "#673AB7"

    const val TRUE = 1
    
    const val DEFAULT_TASK_LIST_ID = 0
    
    const val DATE_FORMAT = "HH:mm, dd/MM/yyyy"
}

object Database {
    const val DATABASE_NAME = "GET_IT_DONE_DATABASE"
    const val DATABASE_VERSION = 1

    const val SQL_CREATE_TABLE_LIST = "CREATE TABLE ${TaskList.TABLE_NAME} (" +
            "${TaskList.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "${TaskList.TITLE} TEXT)"

    const val SQL_CREATE_TABLE_TASK = "CREATE TABLE ${Task.TABLE_NAME} (" +
            "${Task.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "${Task.LIST_ID} INTEGER, " +
            "${Task.TITLE} TEXT, " +
            "${Task.DESCRIPTION} TEXT, " +
            "${Task.TIME_REMINDER} TEXT, " +
            "${Task.IS_IMPORTANT} TEXT, " +
            "${Task.IS_IN_MY_DAY} TEXT, " +
            "${Task.COLOR} TEXT, " +
            "${Task.STATUS} TEXT, " +
            "${Task.TIME_CREATED} TEXT)"

    const val SQL_DROP_TABLE_LIST = "DROP TABLE IF EXITS LIST"

    const val SQL_DROP_TABLE_TASK = "DROP TABLE IF EXITS TASK"

}
