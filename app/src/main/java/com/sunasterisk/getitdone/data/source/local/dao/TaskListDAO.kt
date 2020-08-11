package com.sunasterisk.getitdone.data.source.local.dao

import com.sunasterisk.getitdone.data.model.TaskList

interface TaskListDAO {
    
    fun getTaskListFromId(id: Int): TaskList 
    
    fun getAllLists(): List<TaskList>

    fun addNewList(list: TaskList): Long

    fun changeListTitle(list: TaskList): Boolean

    fun deleteList(list: TaskList): Boolean
}
