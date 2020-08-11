package com.sunasterisk.getitdone.ui.newTaskList

import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import java.lang.Exception

class NewTaskListPresenter(private val taskListRepository: TaskListRepository) :
    NewTaskListContract.Presenter {

    private var view: NewTaskListContract.View? = null

    override fun attach(view: NewTaskListContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun addNewTaskList(taskList: TaskList) {
        taskListRepository.addNewList(taskList, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                val message =
                    if (data) R.string.msg_add_new_list_successfully else R.string.msg_add_new_list_fail
                view?.displayMessage(message)
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
            }
        })
    }
}
