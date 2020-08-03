package com.sunasterisk.getitdone.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseFragment
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.data.source.local.TaskListLocalDataSource
import com.sunasterisk.getitdone.data.source.local.TaskLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAOImpl
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.Constants.STATUS_COMPLETED
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import com.sunasterisk.getitdone.utils.toast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeContract.View, HomePresenter>(),
    HomeContract.View {

    override val layoutId get() = R.layout.fragment_home

    private var presenter: HomePresenter? = null

    private var listId = DEFAULT_TASK_LIST_ID

    private val taskCompletedAdapter = TaskAdapter()
    private val taskUnCompleteAdapter = TaskAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { listId = it.getInt(ARG_LIST_ID) }
        (activity as AppCompatActivity).setSupportActionBar(toolbarHome)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        context?.let {
            val database = AppDatabase.getInstance(it)

            val taskListRepository = TaskListRepository.getInstance(
                TaskListLocalDataSource.getInstance(
                    TaskListDAOImpl.getInstance(database)
                )
            )

            val taskRepository = TaskRepository.getInstance(
                TaskLocalDataSource.getInstance(
                    TaskDAOImpl.getInstance(database)
                )
            )

            presenter = HomePresenter(taskListRepository, taskRepository).apply {
                attach(this@HomeFragment)
                getTaskListFromId(listId)
                getTasksFromTaskListId(listId)
            }
        }

        recyclerViewTasksUnComplete.adapter = taskUnCompleteAdapter.apply {
            onCheckboxClickListener = { onTaskCheckboxClicked(it, false) }
            onImportantClickListener = { onTaskImportantClicked(it, it.isImportant) }
            clickItemListener = { onItemTaskClick(it) }
        }

        recyclerViewTasksCompleted.adapter = taskCompletedAdapter.apply {
            onCheckboxClickListener = { onTaskCheckboxClicked(it, true) }
            onImportantClickListener = { onTaskImportantClicked(it, it.isImportant) }
            clickItemListener = { onItemTaskClick(it) }
        }
    }

    override fun setToolBarTitle(title: String) {
        toolbarHome.title = title
    }

    override fun displayMessage(stringId: Int) {
        context?.run { toast(getString(stringId)) }
    }

    override fun displayMessage(message: String) {
        context?.run { toast(message) }
    }

    override fun handleLoadedUnCompletedTasks(tasks: List<Task>) {
        taskUnCompleteAdapter.loadItems(tasks.toMutableList())
    }

    override fun handleLoadedCompletedTasks(tasks: List<Task>) {
        taskCompletedAdapter.loadItems(tasks.toMutableList())
        updateCompletedTaskTitle()
    }

    private fun onItemTaskClick(taskId: Int) {

    }

    private fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean) {
        task.status =
            if (isCompleted) {
                taskCompletedAdapter.removeItem(task)
                taskUnCompleteAdapter.insertItem(task)
                STATUS_NOT_COMPLETE
            } else {
                taskUnCompleteAdapter.removeItem(task)
                taskCompletedAdapter.insertItem(task)
                STATUS_COMPLETED
            }
        updateCompletedTaskTitle()
        presenter?.updateTask(task)
    }

    private fun onTaskImportantClicked(task: Task, isImportant: Boolean) {
        task.isImportant = !isImportant
        notifyTaskImportantChanged(task)
        presenter?.updateTask(task)
    }

    private fun notifyTaskImportantChanged(task: Task) {
        if (task.status == STATUS_COMPLETED) {
            taskCompletedAdapter.updateItem(task)
        } else {
            taskUnCompleteAdapter.updateItem(task)
        }
    }

    private fun updateCompletedTaskTitle() {
        textCompletedTitle.text =
            "${getString(R.string.msg_completed_task_title)} (${taskCompletedAdapter.items.size})"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detach()
    }

    companion object {
        private const val ARG_LIST_ID = "LIST_ID"
        const val HOME_TAG = "HOME_FRAGMENT"

        fun newInstance(listId: Int) = HomeFragment().apply {
            arguments = bundleOf(ARG_LIST_ID to listId)
        }
    }
}
