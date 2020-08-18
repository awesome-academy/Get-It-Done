package com.sunasterisk.getitdone.ui.newTaskList

import androidx.core.os.bundleOf
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.utils.Constants
import com.sunasterisk.getitdone.utils.Constants.BUNDLE_TASK_LIST_TITLE
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

    override fun getTaskListInfo(id: Int) {
        taskListRepository.getTaskListFromId(id, object : OnLoadedCallback<TaskList> {
            override fun onSuccess(data: TaskList) {
                view?.showListInfo(data)
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
            }
        })
    }

    override fun editTaskList(taskList: TaskList) {
        taskListRepository.changeListTitle(taskList, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                view?.sendRequestUpdateListTitle(
                    Constants.REQUEST_KEY_UPDATE_TASK_LIST_TITLE,
                    bundleOf(BUNDLE_TASK_LIST_TITLE to taskList.title)
                )
                view?.popFragment()
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
                view?.popFragment()
            }
        })
    }

    override fun addNewTaskList(taskList: TaskList) {
        taskListRepository.addNewList(taskList, object : OnLoadedCallback<Long> {
            override fun onSuccess(data: Long) {
                if (data > 0) {
                    view?.onNewTaskListCreated(data)
                } else {
                    view?.displayMessage(R.string.msg_add_new_list_fail)
                }
                view?.popFragment()
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
                view?.popFragment()
            }
        })
    }
}
