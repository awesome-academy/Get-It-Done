package com.sunasterisk.getitdone.ui.options

import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.utils.Constants
import java.lang.Exception

class OptionsPresenter(
    private val taskRepository: TaskRepository,
    private val taskListRepository: TaskListRepository
) :
    OptionsContract.Presenter {

    private var view: OptionsContract.View? = null

    override fun attach(view: OptionsContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun deleteTaskList(id: Int) {
        taskListRepository.deleteList(id, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                if (data) {
                    taskRepository.deleteTasksByListId(id, object : OnLoadedCallback<Boolean> {
                        override fun onSuccess(data: Boolean) {
                            if (data) {
                                view?.onDeleteListSuccessful()
                                view?.dismissFragment()
                            } else {
                                view?.displayMessage(R.string.msg_delete_list_fail)
                                view?.dismissFragment()
                            }
                        }

                        override fun onFailure(exception: Exception) {
                            view?.displayMessage(exception.message.toString())
                            view?.dismissFragment()
                        }
                    })
                } else {
                    view?.displayMessage(R.string.msg_delete_list_fail)
                    view?.dismissFragment()
                }
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.message.toString())
                view?.dismissFragment()
            }
        })
    }

    override fun deleteAllCompletedTask(id: Int) {
        taskRepository.deleteCompletedTasksByListId(id, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                if (data) {
                    view?.displayMessage(R.string.msg_delete_completed_tasks_successful)
                    view?.sendRequest(Constants.REQUEST_KEY_DELETE_COMPLETED_TASKS)
                    view?.dismissFragment()
                } else {
                    view?.displayMessage(R.string.msg_delete_completed_tasks_fail)
                    view?.dismissFragment()
                }
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.message.toString())
                view?.dismissFragment()
            }
        })
    }
}
