package com.sunasterisk.getitdone.data.source.local.dao

import android.database.Cursor
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase

class TaskListDAOImpl private constructor(appDatabase: AppDatabase) : TaskListDAO {

    private val database = appDatabase.writableDatabase

    override fun getTaskListFromId(id: Int): TaskList {
        val cursor =
            database.query(
                TaskList.TABLE_NAME,
                null,
                "${TaskList.ID} =?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        cursor.moveToFirst()
        val taskList = getTaskListFromCursor(cursor)
        cursor.close()
        return taskList
    }

    override fun getAllLists(): List<TaskList> {
        val cursor =
            database.query(TaskList.TABLE_NAME, null, null, null, null, null, null)
        val list = mutableListOf<TaskList>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                list.add(getTaskListFromCursor(cursor))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return list
    }

    override fun addNewList(list: TaskList): Long {
        val values = list.getContentValues()
        return database.insert(TaskList.TABLE_NAME, null, values)
    }

    override fun changeListTitle(list: TaskList): Boolean {
        val values = list.getContentValues()
        return database.update(
            TaskList.TABLE_NAME,
            values,
            "${TaskList.ID} =?",
            arrayOf(list.id.toString())
        ) > 0
    }

    override fun deleteList(listId: Int): Boolean {
        return database.delete(
            TaskList.TABLE_NAME,
            "${TaskList.ID} =?",
            arrayOf(listId.toString())
        ) > 0
    }

    private fun getTaskListFromCursor(cursor: Cursor): TaskList {
        return TaskList(cursor)
    }

    companion object {
        private var instance: TaskListDAOImpl? = null

        fun getInstance(appDatabase: AppDatabase): TaskListDAOImpl =
            instance ?: TaskListDAOImpl(appDatabase).also {
                instance = it
            }
    }
}
