package com.sunasterisk.getitdone.data.source

import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task

interface TaskDataSource {
    interface Local {

        fun getAllTasksByListId(listId: Int, callback: OnLoadedCallback<List<Task>>)

        fun getTaskById(id: Int, callback: OnLoadedCallback<Task>)

        fun searchTasksByTitle(title: String, callback: OnLoadedCallback<List<Task>>)

        fun getTaskInMyDay(callback: OnLoadedCallback<List<Task>>)

        fun getImportantTasks(callback: OnLoadedCallback<List<Task>>)

        fun addNewTask(task: Task, callback: OnLoadedCallback<Boolean>)

        fun updateTask(task: Task, callback: OnLoadedCallback<Boolean>)

        fun changeStatus(task: Task, callback: OnLoadedCallback<Boolean>)

        fun deleteTask(taskId: Int, callback: OnLoadedCallback<Boolean>)

        fun deleteTasksByListId(listId: Int, callback: OnLoadedCallback<Boolean>)
    }

    interface Remote
}
