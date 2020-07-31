package com.sunasterisk.getitdone.utils

object Constants {
    const val DEFAULT_ID = -1
    const val EMPTY_STRING = ""
    const val STATUS_NOT_COMPLETE = 0
    const val STATUS_COMPLETED = 1

    const val DEFAULT_COLOR = "#607D8B"
    const val COLOR_GREEN = "#4CAF50"
    const val COLOR_CYAN = "#00BCD4"
    const val COLOR_LIME = "#CDDC39"
    const val COLOR_ORANGE = "#FFC107"
    const val COLOR_RED = "#F44336"
    const val COLOR_PURPLE = "#673AB7"
}

object Database {
    const val DATABASE_NAME = "GET_IT_DONE_DATABASE"
    const val DATABASE_VERSION = 1

    const val SQL_CREATE_TABLE_LIST = "CREATE TABLE LIST (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TITLE TEXT)"

    const val SQL_CREATE_TABLE_TASK = "CREATE TABLE TASK (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "LIST_ID INTEGER, " +
            "TITLE TEXT, " +
            "DESCRIPTION TEXT, " +
            "TIME_REMINDER TEXT, " +
            "IS_IMPORTANT TEXT, " +
            "IS_IN_MY_DAY TEXT, " +
            "COLOR TEXT, " +
            "STATUS INT, " +
            "TIME_CREATED TEXT)"

    const val SQL_DROP_TABLE_LIST = "DROP TABLE IF EXITS LIST"

    const val SQL_DROP_TABLE_TASK = "DROP TABLE IF EXITS TASK"
}
