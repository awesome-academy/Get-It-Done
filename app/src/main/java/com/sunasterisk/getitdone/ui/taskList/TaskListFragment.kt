package com.sunasterisk.getitdone.ui.taskList

import android.os.Bundle
import androidx.core.os.bundleOf
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseBottomSheetFragment
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.source.local.TaskListLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.toast
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : BaseBottomSheetFragment<TaskListContract.View, TaskListPresenter>(),
    TaskListContract.View {

    override val layoutId get() = R.layout.fragment_task_list

    private var selectedTaskListId = -1

    private var presenter: TaskListPresenter? = null

    private val taskListAdapter = TaskListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { selectedTaskListId = it.getInt(ARG_TASK_LIST_ID) }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        initPresenter()
        initRecyclerView()
    }

    override fun showLoadedTaskLists(taskLists: List<TaskList>) {
        taskListAdapter.loadItems(taskLists.toMutableList())
    }

    override fun displayMessage(message: String) {
        context?.run { toast(message) }
    }

    private fun initPresenter(){
        context?.let {
            val database = AppDatabase.getInstance(it)

            val taskListRepository = TaskListRepository.getInstance(
                TaskListLocalDataSource.getInstance(
                    TaskListDAOImpl.getInstance(database)
                )
            )

            presenter = TaskListPresenter(taskListRepository).apply {
                attach(this@TaskListFragment)
                getAllTaskList()
            }
        }
    }
    
    private fun initRecyclerView(){
        recyclerViewTaskLists.adapter = taskListAdapter.apply {
            selectedId = selectedTaskListId
            clickItemListener = { onItemClick(it) }
        }
    }
    
    private fun onItemClick(taskList: TaskList) {
        switchToTaskList(taskList)
    }

    private fun switchToTaskList(taskList: TaskList) {
    }

    companion object {
        private const val ARG_TASK_LIST_ID = "TASK_LIST_ID"
        const val TASK_LIST_TAG = "TASK_LIST_FRAGMENT"

        fun newInstance(taskListId: Int) = TaskListFragment().apply {
            arguments = bundleOf(ARG_TASK_LIST_ID to taskListId)
        }
    }
}
