package com.sunasterisk.getitdone.data.repository

import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.source.TaskDataSource

class TaskRepository private constructor(
    private val localDataSource: TaskDataSource.Local
) :
    TaskDataSource.Local {

    override fun getAllTasksByListId(listId: Int, callback: OnLoadedCallback<List<Task>>) {
        localDataSource.getAllTasksByListId(listId, callback)
    }

    override fun getTaskById(id: Int, callback: OnLoadedCallback<Task>) {
        localDataSource.getTaskById(id, callback)
    }

    override fun searchTasksByTitle(title: String, callback: OnLoadedCallback<List<Task>>) {
        localDataSource.searchTasksByTitle(title, callback)
    }

    override fun getTaskInMyDay(today: String, callback: OnLoadedCallback<List<Task>>) {
        localDataSource.getTaskInMyDay(today, callback)
    }

    override fun getImportantTasks(callback: OnLoadedCallback<List<Task>>) {
        localDataSource.getImportantTasks(callback)
    }

    override fun addNewTask(task: Task, callback: OnLoadedCallback<Long>) {
        localDataSource.addNewTask(task, callback)
    }

    override fun updateTask(task: Task, callback: OnLoadedCallback<Boolean>) {
        localDataSource.updateTask(task, callback)
    }

    override fun changeStatus(task: Task, callback: OnLoadedCallback<Boolean>) {
        localDataSource.changeStatus(task, callback)
    }

    override fun deleteTask(taskId: Int, callback: OnLoadedCallback<Boolean>) {
        localDataSource.deleteTask(taskId, callback)
    }

    override fun deleteTasksByListId(listId: Int, callback: OnLoadedCallback<Boolean>) {
        localDataSource.deleteTasksByListId(listId, callback)
    }

    override fun deleteCompletedTasksByListId(listId: Int, callback: OnLoadedCallback<Boolean>) {
        localDataSource.deleteCompletedTasksByListId(listId, callback)
    }

    companion object {
        private var instance: TaskRepository? = null

        fun getInstance(localDataSource: TaskDataSource.Local) =
            instance ?: TaskRepository(localDataSource).also {
                instance = it
            }
    }
}
