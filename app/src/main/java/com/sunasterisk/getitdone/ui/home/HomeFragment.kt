package com.sunasterisk.getitdone.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
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
import com.sunasterisk.getitdone.utils.Constants.BUNDLE_TASK
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_DELETE_TASK
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_UPDATE_TASK
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

    private var callback: OnItemTaskClickCallBack? = null

    fun setOnTaskSelectedListener(callback: OnItemTaskClickCallBack) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { listId = it.getInt(ARG_LIST_ID) }

        setFragmentResultListener(REQUEST_KEY_UPDATE_TASK) { _, bundle ->
            val result = bundle.getParcelable<Task>(BUNDLE_TASK)
            result?.let { onUpdateTask(it) }
        }

        setFragmentResultListener(REQUEST_KEY_DELETE_TASK) { _, bundle ->
            val result = bundle.getParcelable<Task>(BUNDLE_TASK)
            result?.let { onDeleteTask(it) }
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbarHome)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        initPresenter()
        initUnCompletedTasks()
        initCompletedTasks()
    }

    override fun setToolBarTitle(title: String) {
        toolbarHome.title = title
    }

    override fun setToolBarTitle(stringId: Int) {
        toolbarHome.title = getString(stringId)
    }

    override fun displayMessage(stringId: Int) {
        context?.run { toast(getString(stringId)) }
    }

    override fun displayMessage(message: String) {
        context?.run { toast(message) }
    }

    override fun showLoadedUnCompletedTasks(tasks: List<Task>) {
        taskUnCompleteAdapter.loadItems(tasks.toMutableList())
    }

    override fun showLoadedCompletedTasks(tasks: List<Task>) {
        taskCompletedAdapter.loadItems(tasks.toMutableList())
        updateCompletedTaskTitle()
    }

    private fun initPresenter() {
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
    }

    private fun initUnCompletedTasks() {
        recyclerViewTasksUnComplete.adapter = taskUnCompleteAdapter.apply {
            onCheckboxClickListener = { onTaskCheckboxClicked(it) }
            onImportantClickListener = { onTaskImportantClicked(it) }
            clickItemListener = { onItemTaskClick(it) }
        }
    }

    private fun initCompletedTasks() {
        recyclerViewTasksCompleted.adapter = taskCompletedAdapter.apply {
            onCheckboxClickListener = { onTaskCheckboxClicked(it) }
            onImportantClickListener = { onTaskImportantClicked(it) }
            clickItemListener = { onItemTaskClick(it) }
        }
    }

    private fun onItemTaskClick(task: Task) {
        callback?.onItemTaskClick(task)
    }

    private fun onTaskCheckboxClicked(task: Task) {
        if (task.status == STATUS_COMPLETED) {
            taskCompletedAdapter.removeItem(task)
            taskUnCompleteAdapter.insertItem(task)
            task.status = STATUS_NOT_COMPLETE
        } else {
            taskUnCompleteAdapter.removeItem(task)
            taskCompletedAdapter.insertItem(task)
            task.status = STATUS_COMPLETED
        }
        updateCompletedTaskTitle()
        presenter?.updateTask(task)
    }

    private fun onTaskImportantClicked(task: Task) {
        task.isImportant = !task.isImportant
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

    private fun onUpdateTask(task: Task) {
        taskCompletedAdapter.items.find { it.id == task.id }?.apply {
            val index = taskCompletedAdapter.items.indexOf(this)
            taskCompletedAdapter.items[index] = task
            taskCompletedAdapter.updateItem(task)
            if (task.status != STATUS_COMPLETED) onTaskCheckboxClicked(task)
        }

        taskUnCompleteAdapter.items.find { it.id == task.id }?.apply {
            val index = taskUnCompleteAdapter.items.indexOf(this)
            taskUnCompleteAdapter.items[index] = task
            taskUnCompleteAdapter.updateItem(task)
            if (task.status != STATUS_NOT_COMPLETE) onTaskCheckboxClicked(task)
        }
    }

    private fun onDeleteTask(task: Task) {
        if (task.status == STATUS_COMPLETED) {
            taskCompletedAdapter.items.find { it.id == task.id }?.apply {
                taskCompletedAdapter.removeItem(this)
            }
        } else {
            taskUnCompleteAdapter.items.find { it.id == task.id }?.apply {
                taskUnCompleteAdapter.removeItem(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detach()
    }

    interface OnItemTaskClickCallBack {
        fun onItemTaskClick(task: Task)
    }

    companion object {
        private const val ARG_LIST_ID = "LIST_ID"
        const val HOME_TAG = "HOME_FRAGMENT"

        fun newInstance(listId: Int) = HomeFragment().apply {
            arguments = bundleOf(ARG_LIST_ID to listId)
        }
    }
}
