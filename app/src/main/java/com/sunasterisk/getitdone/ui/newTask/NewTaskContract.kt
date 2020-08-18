package com.sunasterisk.getitdone.ui.newTask

import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.Task

interface NewTaskContract {
    interface View : BaseContract.View {
        fun onNewTaskCreated(id: Long)
        fun displayMessage(message: String)
        fun displayMessage(stringId: Int)
        fun dismissFragment()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun addNewTask(task: Task)
    }
}
