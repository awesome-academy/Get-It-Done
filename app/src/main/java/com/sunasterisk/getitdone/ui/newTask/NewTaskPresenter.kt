package com.sunasterisk.getitdone.ui.newTask

import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.repository.TaskRepository
import java.lang.Exception

class NewTaskPresenter(private val taskRepository: TaskRepository) :
    NewTaskContract.Presenter {

    private var view: NewTaskContract.View? = null

    override fun attach(view: NewTaskContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun addNewTask(task: Task) {
        taskRepository.addNewTask(task, object : OnLoadedCallback<Long> {
            override fun onSuccess(data: Long) {
                if (data > 0) {
                    view?.onNewTaskCreated(data)
                } else {
                    view?.displayMessage(R.string.msg_add_new_task_fail)
                }
                view?.dismissFragment()
            }

            override fun onFailure(exception: Exception) {
                view?.displayMessage(exception.toString())
                view?.dismissFragment()
            }
        })
    }
}
