package com.sunasterisk.getitdone.data.source.local.dao

import com.sunasterisk.getitdone.data.model.Task

interface TaskDAO {
    
    fun getAllTasksByListId(listId: Int): List<Task>

    fun getTaskById(id: Int): Task
    
    fun getCompletedTasks() : List<Task>
    
    fun getUnCompletedTasks() : List<Task>

    fun searchTasksByTitle(title: String): List<Task>

    fun getTaskInMyDay(today: String): List<Task>

    fun getImportantTasks(): List<Task>

    fun addNewTask(task: Task): Boolean

    fun updateTask(task: Task): Boolean

    fun changeStatus(task: Task): Boolean

    fun deleteTask(taskId: Int): Boolean

    fun deleteTasksByListId(listId: Int): Boolean
}
