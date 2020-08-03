package com.sunasterisk.getitdone.data.source.local

import com.sunasterisk.getitdone.data.LoadingAsyncTask
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.source.TaskListDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAO

class TaskListLocalDataSource private constructor(
    private val taskListDAO: TaskListDAO
) : TaskListDataSource.Local {

    override fun getTaskListFromId(id: Int, callback: OnLoadedCallback<TaskList>) {
        LoadingAsyncTask<Int, TaskList>(callback) {
            taskListDAO.getTaskListFromId(id)
        }.execute(id)
    }

    override fun getAllLists(callback: OnLoadedCallback<List<TaskList>>) {
        LoadingAsyncTask<Void, List<TaskList>>(callback) {
            taskListDAO.getAllLists()
        }.execute()
    }

    override fun addNewList(list: TaskList, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<TaskList, Boolean>(callback) {
            taskListDAO.addNewList(list)
        }.execute(list)
    }

    override fun changeListTitle(list: TaskList, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<TaskList, Boolean>(callback) {
            taskListDAO.changeListTitle(list)
        }.execute(list)
    }

    override fun deleteList(list: TaskList, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<TaskList, Boolean>(callback) {
            taskListDAO.deleteList(list)
        }.execute(list)
    }

    companion object {
        private var instance: TaskListLocalDataSource? = null

        fun getInstance(taskListDAO: TaskListDAO) =
            instance ?: TaskListLocalDataSource(taskListDAO).also {
                instance = it
            }
    }
}
