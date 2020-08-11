package com.sunasterisk.getitdone.ui.home

import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.utils.Constants

class HomePresenter(
    private val taskListRepository: TaskListRepository,
    private val taskRepository: TaskRepository
) : HomeContract.Presenter {

    private var view: HomeContract.View? = null

    override fun attach(view: HomeContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun getTaskListFromId(id: Int) {
        taskListRepository.getTaskListFromId(id, object : OnLoadedCallback<TaskList> {
            override fun onSuccess(data: TaskList) {
                view?.setToolBarTitle(data.title)
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
            }
        })
    }

    override fun getTasksFromTaskListId(listId: Int) {
        taskRepository.getAllTasksByListId(listId, object : OnLoadedCallback<List<Task>> {
            override fun onSuccess(data: List<Task>) {
                val unCompleteTasks = mutableListOf<Task>()
                val completedTasks = mutableListOf<Task>()
                for (task in data) {
                    if (task.status == Constants.STATUS_NOT_COMPLETE) {
                        unCompleteTasks.add(task)
                    } else {
                        completedTasks.add(task)
                    }
                }
                view?.showLoadedUnCompletedTasks(unCompleteTasks)
                view?.showLoadedCompletedTasks(completedTasks)
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
            }
        })
    }

    override fun updateTask(task: Task) {
        taskRepository.updateTask(task, object : OnLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                val message =
                    if (data) R.string.msg_update_task_successfully else R.string.msg_update_task_fail
                view?.displayMessage(message)
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
            }
        })
    }
}
