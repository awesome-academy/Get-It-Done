package com.sunasterisk.getitdone.ui.home

import android.app.SearchManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.ConcatAdapter
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
import com.sunasterisk.getitdone.utils.Constants.BUNDLE_TASK_LIST_TITLE
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_DELETE_COMPLETED_TASKS
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_DELETE_TASK
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_UPDATE_TASK
import com.sunasterisk.getitdone.utils.Constants.REQUEST_KEY_UPDATE_TASK_LIST_TITLE
import com.sunasterisk.getitdone.utils.Constants.STATUS_COMPLETED
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import com.sunasterisk.getitdone.utils.toast
import com.sunasterisk.getitdone.widget.TaskWidget
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeContract.View, HomePresenter>(),
    HomeContract.View, SearchView.OnQueryTextListener {

    override val layoutId get() = R.layout.fragment_home

    private var presenter: HomePresenter? = null

    private var listId = DEFAULT_TASK_LIST_ID

    private val taskCompletedAdapter = TaskAdapter()
    private val taskUnCompleteAdapter = TaskAdapter()
    private val taskCompletedTaskTitleAdapter = CompletedTaskTitleAdapter()

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

        setFragmentResultListener(REQUEST_KEY_UPDATE_TASK_LIST_TITLE) { _, bundle ->
            bundle.getString(BUNDLE_TASK_LIST_TITLE)?.let { setToolBarTitle(it) }
        }

        setFragmentResultListener(REQUEST_KEY_DELETE_COMPLETED_TASKS) { _, _ ->
            onDeleteCompletedTasks()
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_search_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (searchMenuItem.actionView as SearchView).apply {
            queryHint = getString(R.string.msg_search_hint)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(this@HomeFragment)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(toolbarHome)
        initPresenter()
        initBottomBar()
        initAdapter()
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

    override fun showInsertedTask(task: Task) {
        taskUnCompleteAdapter.insertItem(task)
        updateWidgetTasks()
    }

    override fun showLoadedUnCompletedTasks(tasks: List<Task>) {
        taskUnCompleteAdapter.loadItems(tasks.toMutableList())
        updateWidgetTasks()
    }

    override fun showLoadedCompletedTasks(tasks: List<Task>) {
        taskCompletedAdapter.loadItems(tasks.toMutableList())
        taskCompletedTaskTitleAdapter.loadItems(mutableListOf(taskCompletedAdapter.items.size))
    }

    override fun onQueryTextSubmit(query: String): Boolean = false

    override fun onQueryTextChange(newText: String): Boolean {
        taskUnCompleteAdapter.filter(newText)
        taskCompletedAdapter.filter(newText)
        updateCompletedTaskTitle()
        return true
    }

    fun onInsertNewTask(taskId: Int) {
        presenter?.getTaskFromId(taskId)
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

    private fun initBottomBar() {
        bottomBar.setNavigationOnClickListener { callback?.navigateTaskLists() }
        bottomBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_bar_settings -> {
                    callback?.openSettings()
                    true
                }
                else -> false
            }
        }
        fabAddNewTask.setOnClickListener {
            callback?.onAddNewTask()
        }
    }

    private fun initAdapter() {
        taskUnCompleteAdapter.apply {
            onCheckboxClickListener = { onTaskCheckboxClicked(it) }
            onImportantClickListener = { onTaskImportantClicked(it) }
            clickItemListener = { onItemTaskClick(it) }
        }

        taskCompletedAdapter.apply {
            onCheckboxClickListener = { onTaskCheckboxClicked(it) }
            onImportantClickListener = { onTaskImportantClicked(it) }
            clickItemListener = { onItemTaskClick(it) }
        }

        recyclerViewTasks.adapter = ConcatAdapter(
            taskUnCompleteAdapter,
            taskCompletedTaskTitleAdapter,
            taskCompletedAdapter
        )
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
        updateWidgetTasks()
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
        taskCompletedTaskTitleAdapter.items[0] = taskCompletedAdapter.items.size
        taskCompletedTaskTitleAdapter.notifyItemChanged(0)
    }

    private fun onUpdateTask(task: Task) {
        taskCompletedAdapter.items.find { it.id == task.id }?.apply {
            val index = taskCompletedAdapter.items.indexOf(this)
            taskCompletedAdapter.items[index] = task
            taskCompletedAdapter.updateItem(task)
            if (task.status != STATUS_COMPLETED) {
                taskCompletedAdapter.removeItemAt(index)
                taskUnCompleteAdapter.insertItem(task)
                updateCompletedTaskTitle()
            }
        }

        taskUnCompleteAdapter.items.find { it.id == task.id }?.apply {
            val index = taskUnCompleteAdapter.items.indexOf(this)
            taskUnCompleteAdapter.items[index] = task
            taskUnCompleteAdapter.updateItem(task)
            if (task.status != STATUS_NOT_COMPLETE) {
                taskUnCompleteAdapter.removeItemAt(index)
                taskCompletedAdapter.insertItem(task)
                updateCompletedTaskTitle()
            }
        }

        updateWidgetTasks()
    }

    private fun onDeleteTask(task: Task) {
        if (task.status == STATUS_COMPLETED) {
            taskCompletedAdapter.items.find { it.id == task.id }?.apply {
                taskCompletedAdapter.removeItem(this)
            }
        } else {
            taskUnCompleteAdapter.items.find { it.id == task.id }?.apply {
                taskUnCompleteAdapter.removeItem(this)
                updateWidgetTasks()
            }
        }
    }

    private fun onDeleteCompletedTasks() {
        taskCompletedAdapter.removeAllItems()
        updateCompletedTaskTitle()
    }

    private fun updateWidgetTasks() {
        val intent = Intent(context, TaskWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        context?.let { context ->
            val idArr = AppWidgetManager.getInstance(context)?.getAppWidgetIds(
                ComponentName(context.applicationContext, TaskWidget::class.java)
            )
            idArr?.let {
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, it)
                activity?.sendBroadcast(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detach()
    }

    interface OnItemTaskClickCallBack {
        fun onItemTaskClick(task: Task)
        fun onAddNewTask()
        fun navigateTaskLists()
        fun openSettings()
    }

    companion object {
        private const val ARG_LIST_ID = "LIST_ID"
        const val HOME_TAG = "HOME_FRAGMENT"

        fun newInstance(listId: Int) = HomeFragment().apply {
            arguments = bundleOf(ARG_LIST_ID to listId)
        }
    }
}
