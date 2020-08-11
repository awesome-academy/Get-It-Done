package com.sunasterisk.getitdone.ui.detail

import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.utils.Constants.BUNDLE_TASK
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_DELETE_TASK
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_UPDATE_TASK

class DetailPresenter(
    private val taskListRepository: TaskListRepository,
    private val taskRepository: TaskRepository
) : DetailContract.Presenter {

    private var view: DetailContract.View? = null

    override fun attach(view: DetailContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun getAllTaskList() {
        taskListRepository.getAllLists(object : OnLoadedCallback<List<TaskList>> {
            override fun onSuccess(data: List<TaskList>) {
                view?.showTaskLists(data)
            }

            override fun onFailure(exception: Exception) {
                view?.showMessage(exception.message.toString())
            }
        })
    }

    override fun updateTask(task: Task) {
        taskRepository.updateTask(task, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                if (data) {
                    view?.sendRequest(REQUEST_KEY_UPDATE_TASK, BUNDLE_TASK)
                    view?.showMessage(R.string.msg_update_task_successfully)
                    view?.popFragment()
                } else {
                    view?.showMessage(R.string.msg_update_task_fail)
                    view?.popFragment()
                }
            }

            override fun onFailure(exception: Exception) {
                view?.showMessage(exception.message.toString())
                view?.popFragment()
            }
        })
    }

    override fun deleteTask(id: Int) {
        taskRepository.deleteTask(id, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                if (data) {
                    view?.sendRequest(REQUEST_KEY_DELETE_TASK, BUNDLE_TASK)
                    view?.showMessage(R.string.msg_delete_task_successfully)
                    view?.popFragment()
                } else {
                    view?.showMessage(R.string.msg_delete_task_fail)
                    view?.popFragment()
                }
            }

            override fun onFailure(exception: Exception) {
                view?.showMessage(exception.message.toString())
                view?.popFragment()
            }
        })
    }
}
