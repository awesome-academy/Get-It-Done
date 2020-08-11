package com.sunasterisk.getitdone.data.source

import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.TaskList

interface TaskListDataSource {
    interface Local {

        fun getTaskListFromId(id: Int, callback: OnLoadedCallback<TaskList>)

        fun getAllLists(callback: OnLoadedCallback<List<TaskList>>)

        fun addNewList(list: TaskList, callback: OnLoadedCallback<Long>)

        fun changeListTitle(list: TaskList, callback: OnLoadedCallback<Boolean>)

        fun deleteList(list: TaskList, callback: OnLoadedCallback<Boolean>)
    }

    interface Remote
}
