package com.sunasterisk.getitdone.ui.newTask

import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.repository.TaskRepository

class NewTaskPresenter(private val taskRepository: TaskRepository) :
    NewTaskContract.Presenter {

    override fun attach(view: NewTaskContract.View) {
    }

    override fun detach() {
    }
    
    override fun addNewTask(task: Task) {
    }
}
