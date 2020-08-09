package com.sunasterisk.getitdone.data.source.local.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sunasterisk.getitdone.utils.Database

class AppDatabase private constructor(context: Context, dbName: String, version: Int) :
    SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.run {
            execSQL(Database.SQL_CREATE_TABLE_LIST)
            execSQL(Database.SQL_CREATE_TABLE_TASK)
            execSQL(Database.SQL_CREATE_DEFAULT_TASK_LIST)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.run {
            execSQL(Database.SQL_DROP_TABLE_LIST)
            execSQL(Database.SQL_DROP_TABLE_TASK)
        }

        onCreate(db)
    }

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase =
            instance ?: AppDatabase(context, Database.DATABASE_NAME, Database.DATABASE_VERSION).also {
                instance = it
            }
    }
}
