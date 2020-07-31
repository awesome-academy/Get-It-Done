package com.sunasterisk.getitdone.ui.home

import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.data.source.local.TaskListLocalDataSource
import com.sunasterisk.getitdone.data.source.local.TaskLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAOImpl
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import java.lang.Exception

class HomePresenter private constructor() : HomeContract.Presenter {

    private var view: HomeContract.View? = null

    private lateinit var taskListRepository: TaskListRepository
    private lateinit var taskRepository: TaskRepository

    override fun attach(view: HomeContract.View) {
        this.view = view

        val database = AppDatabase.getInstance(view.getParentContext())

        taskListRepository = TaskListRepository.getInstance(
            TaskListLocalDataSource.getInstance(
                TaskListDAOImpl.getInstance(database)
            )
        )

        taskRepository = TaskRepository.getInstance(
            TaskLocalDataSource.getInstance(
                TaskDAOImpl.getInstance(database)
            )
        )
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
                view?.handleException(exception)
            }
        })
    }

    override fun getTasksFromTaskListId(listId: Int) {
        taskRepository.getAllTasksByListId(listId, object : OnLoadedCallback<List<Task>> {
            override fun onSuccess(data: List<Task>) {
                view?.handleLoadedTasks(data)
            }

            override fun onFailure(exception: Exception) {
                view?.handleException(exception)
            }
        })
    }

    companion object {
        private var instance: HomePresenter? = null

        fun getInstance(): HomePresenter =
            instance ?: HomePresenter().also { instance = it }
    }
}
