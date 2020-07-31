package com.sunasterisk.getitdone.data.source.local.dao

import android.database.Cursor
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.Constants.STATUS_COMPLETED
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import com.sunasterisk.getitdone.utils.Constants.TRUE

class TaskDAOImpl private constructor(appDatabase: AppDatabase) : TaskDAO {

    private val database = appDatabase.writableDatabase

    override fun getAllTasksByListId(listId: Int): List<Task> {
        return getTaskInSpecificCondition(Task.LIST_ID, listId.toString())
    }

    override fun getTaskById(id: Int): Task {
        val cursor =
            database.query(
                Task.TABLE_NAME,
                null,
                Task.ID,
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        cursor.close()
        return getTaskFromCursor(cursor)
    }

    override fun getCompletedTasks(): List<Task> {
        val cursor =
            database.query(
                Task.TABLE_NAME,
                null,
                Task.STATUS,
                arrayOf(STATUS_COMPLETED),
                null,
                null,
                null
            )

        val list = mutableListOf<Task>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                list.add(getTaskFromCursor(cursor))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return list
    }

    override fun getUnCompletedTasks(): List<Task> {
        val cursor =
            database.query(
                Task.TABLE_NAME,
                null,
                Task.STATUS,
                arrayOf(STATUS_NOT_COMPLETE),
                null,
                null,
                null
            )

        val list = mutableListOf<Task>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                list.add(getTaskFromCursor(cursor))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return list
    }

    override fun searchTasksByTitle(title: String): List<Task> {
        return getTaskInSpecificCondition(Task.TITLE, title)
    }

    override fun getTaskInMyDay(): List<Task> {
        return getTaskInSpecificCondition(Task.IS_IN_MY_DAY, "$TRUE")
    }

    override fun getImportantTasks(): List<Task> {
        return getTaskInSpecificCondition(Task.IS_IMPORTANT, "$TRUE")
    }

    override fun addNewTask(task: Task): Boolean {
        val values = task.getContentValues()
        return database.insert(Task.TABLE_NAME, null, values) > 0
    }

    override fun updateTask(task: Task): Boolean {
        val values = task.getContentValues()
        return database.update(
            Task.TABLE_NAME,
            values,
            "${Task.ID} =?",
            arrayOf(task.id.toString())
        ) > 0
    }

    override fun changeStatus(task: Task): Boolean {
        val values = task.getContentValues()
        return database.update(
            Task.TABLE_NAME,
            values,
            "${Task.ID} =?",
            arrayOf(task.id.toString())
        ) > 0
    }

    override fun deleteTask(taskId: Int): Boolean {
        return database.delete(
            Task.TABLE_NAME,
            "${Task.ID} =?",
            arrayOf(taskId.toString())
        ) > 0
    }

    override fun deleteTasksByListId(listId: Int): Boolean {
        return database.delete(
            Task.TABLE_NAME,
            "${Task.LIST_ID} =?",
            arrayOf(listId.toString())
        ) > 0
    }

    private fun getTaskInSpecificCondition(selection: String, selectionArg: String): List<Task> {
        val cursor =
            database.query(
                Task.TABLE_NAME,
                null,
                selection,
                arrayOf(selectionArg),
                null,
                null,
                null
            )
        val list = mutableListOf<Task>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                list.add(getTaskFromCursor(cursor))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return list
    }

    private fun getTaskFromCursor(cursor: Cursor): Task {
        return Task(cursor)
    }

    companion object {
        private var instance: TaskDAOImpl? = null

        fun getInstance(appDatabase: AppDatabase): TaskDAOImpl =
            instance ?: TaskDAOImpl(appDatabase).also {
                instance = it
            }
    }
}
