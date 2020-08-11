package com.sunasterisk.getitdone.ui.newTaskList

import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.TaskList

interface NewTaskListContract {
    interface View : BaseContract.View {
        fun onNewTaskListCreated(id:Long)
        fun displayMessage(message: String)
        fun displayMessage(stringId: Int)
        fun popFragment()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun addNewTaskList(taskList: TaskList)
    }
}
