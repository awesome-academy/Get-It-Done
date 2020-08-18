package com.sunasterisk.getitdone.ui.options

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseBottomSheetFragment
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.data.source.local.TaskListLocalDataSource
import com.sunasterisk.getitdone.data.source.local.TaskLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAOImpl
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.Constants.TASK_LIST_IMPORTANT_ID
import com.sunasterisk.getitdone.utils.Constants.TASK_LIST_MY_DAY_ID
import com.sunasterisk.getitdone.utils.toast
import kotlinx.android.synthetic.main.fragment_options.*

class OptionsFragment : BaseBottomSheetFragment<OptionsContract.View, OptionsPresenter>(),
    OptionsContract.View {

    override val layoutId get() = R.layout.fragment_options

    private var taskListId = -1

    private var presenter: OptionsContract.Presenter? = null

    private var callback: OnDeleteList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { taskListId = it.getInt(ARG_TASK_LIST_ID) }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        initView()
        initPresenter()
        initListener()
    }

    override fun displayMessage(message: String) {
        context?.run { toast(message) }
    }

    override fun displayMessage(stringId: Int) {
        context?.run { toast(getString(stringId)) }
    }

    override fun sendRequest(requestKey: String) {
        setFragmentResult(requestKey, bundleOf())
    }

    override fun onDeleteListSuccessful() {
        callback?.onListDeleted()
    }

    override fun dismissFragment() {
        this.dismiss()
    }

    fun setOnDeleteListListener(callback: OnDeleteList) {
        this.callback = callback
    }

    private fun initView() {
        val defaultList =
            intArrayOf(DEFAULT_TASK_LIST_ID, TASK_LIST_MY_DAY_ID, TASK_LIST_IMPORTANT_ID)
        if (taskListId in defaultList) {
            layoutDeleteList.isEnabled = false
            textDefaultList.visibility = View.VISIBLE
            textDeleteList.setTextColor(Color.DKGRAY)
        }
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

            presenter = OptionsPresenter(taskRepository, taskListRepository).apply {
                attach(this@OptionsFragment)
            }
        }
    }

    private fun initListener() {
        textRenameList.setOnClickListener {
            callback?.onRenameList()
            dismissFragment()
        }

        layoutDeleteList.setOnClickListener {
            presenter?.deleteTaskList(taskListId)
        }

        textDeleteAllCompletedTask.setOnClickListener {
            presenter?.deleteAllCompletedTask(taskListId)
        }
    }

    interface OnDeleteList {
        fun onRenameList()
        fun onListDeleted()
    }

    companion object {
        private const val ARG_TASK_LIST_ID = "TASK_LIST_ID"
        const val OPTIONS_TAG = "OPTIONS_FRAGMENT"

        fun newInstance(taskListId: Int) = OptionsFragment().apply {
            arguments = bundleOf(ARG_TASK_LIST_ID to taskListId)
        }
    }
}
