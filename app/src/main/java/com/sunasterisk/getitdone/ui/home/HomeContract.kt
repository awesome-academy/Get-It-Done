package com.sunasterisk.getitdone.ui.home

import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.Task

class HomeContract {
    interface View : BaseContract.View {
        fun setToolBarTitle(title: String)
        fun setToolBarTitle(stringId: Int)
        fun displayMessage(stringId: Int)
        fun displayMessage(message: String)
        fun showLoadedUnCompletedTasks(tasks: List<Task>)
        fun showLoadedCompletedTasks(tasks: List<Task>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getTaskListFromId(id: Int)
        fun getTasksFromTaskListId(listId: Int)
        fun updateTask(task: Task)
    }
}
