package com.sunasterisk.getitdone.data.repository

import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.source.TaskListDataSource

class TaskListRepository private constructor(
    private val localDataSource: TaskListDataSource.Local
) :
    TaskListDataSource.Local {

    override fun getTaskListFromId(id: Int, callback: OnLoadedCallback<TaskList>) {
        localDataSource.getTaskListFromId(id, callback)
    }

    override fun getAllLists(callback: OnLoadedCallback<List<TaskList>>) {
        localDataSource.getAllLists(callback)
    }

    override fun addNewList(list: TaskList, callback: OnLoadedCallback<Boolean>) {
        localDataSource.addNewList(list, callback)
    }

    override fun changeListTitle(list: TaskList, callback: OnLoadedCallback<Boolean>) {
        localDataSource.changeListTitle(list, callback)
    }

    override fun deleteList(list: TaskList, callback: OnLoadedCallback<Boolean>) {
        localDataSource.deleteList(list, callback)
    }

    companion object {
        private var instance: TaskListRepository? = null

        fun getInstance(localDataSource: TaskListDataSource.Local) =
            instance ?: TaskListRepository(localDataSource).also {
                instance = it
            }
    }
}
