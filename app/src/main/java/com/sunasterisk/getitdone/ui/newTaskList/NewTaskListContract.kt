package com.sunasterisk.getitdone.ui.newTaskList

import android.os.Bundle
import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.TaskList

interface NewTaskListContract {
    interface View : BaseContract.View {
        fun onNewTaskListCreated(id: Long)
        fun showListInfo(taskList: TaskList)
        fun displayMessage(message: String)
        fun displayMessage(stringId: Int)
        fun sendRequestUpdateListTitle(requestKey: String, bundle: Bundle)
        fun popFragment()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getTaskListInfo(id: Int)
        fun editTaskList(taskList: TaskList)
        fun addNewTaskList(taskList: TaskList)
    }
}
