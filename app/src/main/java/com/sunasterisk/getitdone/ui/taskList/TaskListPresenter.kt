package com.sunasterisk.getitdone.ui.taskList

import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import java.lang.Exception

class TaskListPresenter(private val taskListRepository: TaskListRepository) :
    TaskListContract.Presenter {

    private var view: TaskListContract.View? = null

    override fun attach(view: TaskListContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun getAllTaskList() {
        taskListRepository.getAllLists(object : OnLoadedCallback<List<TaskList>> {
            override fun onSuccess(data: List<TaskList>) {
                view?.showLoadedTaskLists(data)
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
            }
        })
    }
}
