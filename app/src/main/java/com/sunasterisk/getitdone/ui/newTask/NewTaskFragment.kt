package com.sunasterisk.getitdone.ui.newTask

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseBottomSheetFragment
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.data.source.local.TaskLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.*
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_COLOR
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.Constants.DRAWABLE_RIGHT
import com.sunasterisk.getitdone.utils.Constants.TASK_LIST_IMPORTANT_ID
import com.sunasterisk.getitdone.utils.Constants.TASK_LIST_MY_DAY_ID
import kotlinx.android.synthetic.main.fragment_new_task.*
import java.util.*

class NewTaskFragment : BaseBottomSheetFragment<NewTaskContract.View, NewTaskPresenter>(),
    NewTaskContract.View {

    override val layoutId get() = R.layout.fragment_new_task

    private var taskListId = -1
    private var taskColor = DEFAULT_COLOR

    private var presenter: NewTaskPresenter? = null

    private var callback: OnNewTaskCreated? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { taskListId = it.getInt(ARG_TASK_LIST_ID) }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        initPresenter()
        initListener()
    }

    override fun onNewTaskCreated(id: Long) {
        callback?.onNewTaskCreated(id.toInt())
    }

    override fun displayMessage(message: String) {
        context?.run { toast(message) }
    }

    override fun displayMessage(stringId: Int) {
        context?.run { toast(getString(stringId)) }
    }

    override fun dismissFragment() {
        this.dismiss()
    }

    fun setOnNewTaskCreatedListener(callback: OnNewTaskCreated) {
        this.callback = callback
    }

    private fun initPresenter() {
        context?.let {
            val database = AppDatabase.getInstance(it)

            val taskRepository = TaskRepository.getInstance(
                TaskLocalDataSource.getInstance(
                    TaskDAOImpl.getInstance(database)
                )
            )

            presenter = NewTaskPresenter(taskRepository).apply {
                attach(this@NewTaskFragment)
            }
        }
    }

    private fun initListener() {
        for (item in linearCardView.children) {
            if (item is CardView) item.setOnClickListener { onColorPicker(item.tag as String) }
        }
        imageDatePicker.setOnClickListener { showDateTimePicker() }
        buttonSave.setOnClickListener { saveNewTask() }
        textDatePicker.setOnTouchDrawableEndListener(
            onMainTouch = { showDateTimePicker() },
            onDrawableTouch = { hideTextDatePicker() })
    }

    private fun hideTextDatePicker() {
        textDatePicker.visibility = View.GONE
    }

    private fun onColorPicker(tag: String) {
        taskColor = tag
        imageDatePicker.setColorFilter(Color.parseColor(tag))
        buttonSave.setTextColor(Color.parseColor(tag))
    }

    private fun showDateTimePicker() {
        context?.showDateTimePicker(Calendar.getInstance()) {
            textDatePicker.visibility = View.VISIBLE
            textDatePicker.text = it.time.formatToString()
        }
    }

    private fun saveNewTask() {
        val newTask = Task().apply {
            title = editTextTitle.text.toString()
            description = editTextDescription.text.toString()
            if (textDatePicker.isVisible) timeReminder = textDatePicker.text.toString()
            when (taskListId) {
                TASK_LIST_MY_DAY_ID -> {
                    listId = DEFAULT_TASK_LIST_ID
                    inMyDay = Date().formatToString(Constants.DAY_FORMAT)
                }
                TASK_LIST_IMPORTANT_ID -> {
                    listId = DEFAULT_TASK_LIST_ID
                    isImportant = true
                }
                else -> {
                    listId = taskListId
                }
            }
            color = taskColor
            timeCreated = Date().formatToString()
        }
        presenter?.addNewTask(newTask)
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
