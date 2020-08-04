package com.sunasterisk.getitdone.ui.home

import android.content.Context
import com.sunasterisk.getitdone.base.BaseContract
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList
import java.lang.Exception

class HomeContract {
    interface View : BaseContract.View {
        fun getParentContext(): Context
        fun setToolBarTitle(title: String)
        fun displayMessage(message: String)
        fun handleLoadedTasks(tasks: List<Task>)
        fun handleException(exception: Exception)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getTaskListFromId(id: Int)
        fun getTasksFromTaskListId(listId: Int)
    }
}
