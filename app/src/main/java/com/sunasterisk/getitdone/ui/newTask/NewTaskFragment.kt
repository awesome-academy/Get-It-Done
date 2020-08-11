package com.sunasterisk.getitdone.ui.newTask

import android.os.Bundle
import androidx.core.os.bundleOf
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseBottomSheetFragment

class NewTaskFragment : BaseBottomSheetFragment<NewTaskContract.View, NewTaskPresenter>(),
    NewTaskContract.View {

    override val layoutId get() = R.layout.fragment_new_task

    private var taskListId = -1

    private var callback: OnNewTaskCreated? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { taskListId = it.getInt(ARG_TASK_LIST_ID) }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
    }

    override fun onNewTaskCreated(id: Long) {
    }

    override fun displayMessage(message: String) {
    }

    override fun displayMessage(stringId: Int) {
    }

    override fun popFragment() {
    }

    fun setOnNewTaskCreatedListener(callback: OnNewTaskCreated) {
        this.callback = callback
    }

    interface OnNewTaskCreated {
        fun onNewTaskCreated(id: Int)
    }

    companion object {
        private const val ARG_TASK_LIST_ID = "TASK_LIST_ID"
        const val NEW_TASK_TAG = "NEW_TASK_FRAGMENT"

        fun newInstance(taskListId: Int) = NewTaskFragment().apply {
            arguments = bundleOf(ARG_TASK_LIST_ID to taskListId)
        }
    }
}
