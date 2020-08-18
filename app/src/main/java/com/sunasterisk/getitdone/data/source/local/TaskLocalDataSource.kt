package com.sunasterisk.getitdone.data.source.local

import com.sunasterisk.getitdone.data.LoadingAsyncTask
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.source.TaskDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAO

class TaskLocalDataSource private constructor(
    private val taskDAO: TaskDAO
) : TaskDataSource.Local {

    override fun getAllTasksByListId(listId: Int, callback: OnLoadedCallback<List<Task>>) {
        LoadingAsyncTask<Int, List<Task>>(callback) {
            taskDAO.getAllTasksByListId(listId)
        }.execute(listId)
    }

    override fun getTaskById(id: Int, callback: OnLoadedCallback<Task>) {
        LoadingAsyncTask<Int, Task>(callback) {
            taskDAO.getTaskById(id)
        }.execute(id)
    }

    override fun searchTasksByTitle(title: String, callback: OnLoadedCallback<List<Task>>) {
        LoadingAsyncTask<String, List<Task>>(callback) {
            taskDAO.searchTasksByTitle(title)
        }.execute(title)
    }

    override fun getTaskInMyDay(today: String, callback: OnLoadedCallback<List<Task>>) {
        LoadingAsyncTask<String, List<Task>>(callback) {
            taskDAO.getTaskInMyDay(today)
        }.execute(today)
    }

    override fun getImportantTasks(callback: OnLoadedCallback<List<Task>>) {
        LoadingAsyncTask<Unit, List<Task>>(callback) {
            taskDAO.getImportantTasks()
        }.execute(Unit)
    }

    override fun addNewTask(task: Task, callback: OnLoadedCallback<Long>) {
        LoadingAsyncTask<Task, Long>(callback) {
            taskDAO.addNewTask(task)
        }.execute(task)
    }

    override fun updateTask(task: Task, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<Task, Boolean>(callback) {
            taskDAO.updateTask(task)
        }.execute(task)
    }

    override fun changeStatus(task: Task, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<Task, Boolean>(callback) {
            taskDAO.changeStatus(task)
        }.execute(task)
    }

    override fun deleteTask(taskId: Int, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<Int, Boolean>(callback) {
            taskDAO.deleteTask(taskId)
        }.execute(taskId)
    }

    override fun deleteTasksByListId(listId: Int, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<Int, Boolean>(callback) {
            taskDAO.deleteTasksByListId(listId)
        }.execute(listId)
    }

    override fun deleteCompletedTasksByListId(listId: Int, callback: OnLoadedCallback<Boolean>) {
        LoadingAsyncTask<Int, Boolean>(callback) {
            taskDAO.deleteCompletedTasksByListId(listId)
        }.execute(listId)
    }

    companion object {
        private var instance: TaskLocalDataSource? = null

        fun getInstance(taskDAO: TaskDAO) =
            instance ?: TaskLocalDataSource(taskDAO).also {
                instance = it
            }
    }
}
