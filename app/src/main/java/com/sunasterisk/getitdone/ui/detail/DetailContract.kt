package com.sunasterisk.getitdone.ui.detail

import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList

class DetailContract {
    interface View : BaseContract.View {
        fun sendRequest(requestKey: String, bundleKey: String)
        fun showTaskLists(taskLists: List<TaskList>)
        fun showMessage(string: String)
        fun showMessage(stringId: Int)
        fun popFragment()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getAllTaskList()
        fun updateTask(task: Task)
        fun deleteTask(id: Int)
    }
}
