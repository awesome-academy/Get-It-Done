package com.sunasterisk.getitdone.ui.taskList

import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.TaskList

class TaskListContract {
    interface View : BaseContract.View {
        fun showLoadedTaskLists(taskLists: List<TaskList>)
        fun displayMessage(message: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getAllTaskList()
    }
}
