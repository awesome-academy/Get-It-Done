package com.sunasterisk.getitdone.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseFragment
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.data.source.local.TaskListLocalDataSource
import com.sunasterisk.getitdone.data.source.local.TaskLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAOImpl
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.*
import com.sunasterisk.getitdone.utils.Constants.DAY_FORMAT
import com.sunasterisk.getitdone.utils.Constants.EMPTY_STRING
import com.sunasterisk.getitdone.utils.Constants.STATUS_COMPLETED
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*

class DetailFragment : BaseFragment<DetailContract.View, DetailPresenter>(),
    DetailContract.View, View.OnClickListener {

    override val layoutId get() = R.layout.fragment_detail

    private var taskLists = mutableListOf<TaskList>()
    private var task: Task? = null

    private var presenter: DetailPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { task = it.getParcelable(ARG_TASK) }
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        initPresenter()
        initListener()
        updateUI()
    }

    override fun sendRequest(requestKey: String, bundleKey: String) {
        setFragmentResult(requestKey, bundleOf(bundleKey to task))
    }

    override fun showTaskLists(taskLists: List<TaskList>) {
        this.taskLists.apply {
            clear()
            addAll(taskLists)
        }

        spinnerTaskLists.adapter = SpinnerArrayAdapter(requireContext(), this.taskLists)
        spinnerTaskLists.setSelection(taskLists.indexOf(taskLists.find { it.id == task?.listId }))
    }

    override fun cancelAlarm(task: Task) {
        cancelAlarm(context, task)
    }

    override fun setUpAlarm(task: Task) {
        setUpAlarm(context, task)
    }

    override fun showMessage(string: String) {
        context?.run { toast(string) }
    }

    override fun showMessage(stringId: Int) {
        context?.run { toast(getString(stringId)) }
    }

    override fun popFragment() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> updateTask()

            R.id.btnChangeStatus -> {
                task?.status =
                    if (task?.status == STATUS_COMPLETED) STATUS_NOT_COMPLETE else STATUS_COMPLETED
                updateTask()
            }

            R.id.btnDelete -> deleteTask()

            R.id.imageImportant -> {
                task?.let { it.isImportant = !it.isImportant }
                displayImportantView()
            }

            R.id.constraintMyDay -> {
                task?.let {
                    it.inMyDay = if (it.isInMyDay()) "" else Date().formatToString(DAY_FORMAT)
                }
                displayInMyDayView()
            }

            R.id.constraintDatePicker -> showDateTimePicker()
        }
    }

    override fun onBackPressed() {
        updateTask()
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

            presenter =
                DetailPresenter(taskListRepository, taskRepository).apply {
                    attach(this@DetailFragment)
                    getAllTaskList()
                }
        }
    }

    private fun initListener() {
        btnBack.setOnClickListener(this)
        btnChangeStatus.setOnClickListener(this)
        btnDelete.setOnClickListener(this)

        imageImportant.setOnClickListener(this)

        constraintMyDay.setOnClickListener(this)
        constraintDatePicker.setOnClickListener(this)

        editTextDescription.doOnTextChanged { text, _, _, _ ->
            text?.let { string ->
                if (string.isBlank()) {
                    imageDescription.setColorFilter(Color.DKGRAY)
                } else {
                    task?.let { imageDescription.setColorFilter(Color.parseColor(it.color)) }
                }
            }
        }
    }

    private fun updateUI() {
        task?.let {
            if (it.title.isNotBlank()) editTextTitle.setText(it.title)

            displayImportantView()

            displayDescriptionView()

            displayInMyDayView()

            displayDatePickerView()

            if (it.status == STATUS_COMPLETED) {
                btnChangeStatus.setImageResource(R.drawable.ic_undo)
            } else {
                btnChangeStatus.setImageResource(R.drawable.ic_check)
            }
        }
    }

    private fun displayImportantView() {
        task?.let {
            val imageId = if (it.isImportant) R.drawable.ic_star_on else R.drawable.ic_star_off
            imageImportant.setImageResource(imageId)
        }
    }

    private fun displayDescriptionView() {
        task?.let {
            var color = Color.parseColor(it.color)
            if (it.description.isBlank()) color = Color.DKGRAY
            imageDescription.setColorFilter(color)
            editTextDescription.setTextColor(color)
            editTextDescription.setText(it.description)
        }
    }

    private fun displayInMyDayView() {
        task?.let {
            var color = Color.parseColor(it.color)
            var textInMyDay = R.string.title_added_to_my_day
            if (it.inMyDay != Date().formatToString(DAY_FORMAT)) {
                color = Color.DKGRAY
                textInMyDay = R.string.title_add_to_my_day
            }
            imageMyDay.setColorFilter(color)
            textAddToMyDay.setTextColor(color)
            textAddToMyDay.text = getString(textInMyDay)
        }
    }

    private fun displayDatePickerView() {
        task?.let {
            var color = Color.DKGRAY
            var textDate = getString(R.string.title_add_time_reminder)
            textDatePicker.removeDrawables()
            textDatePicker.background = null
            if (it.timeReminder.isNotBlank()) {
                color = Color.parseColor(it.color)
                textDate = it.timeReminder
                textDatePicker.setBackgroundResource(R.drawable.rounded_corner)
                textDatePicker.addDrawableEnd(R.drawable.ic_close)
                textDatePicker.setOnTouchDrawableEndListener(
                    onDrawableTouch = {
                        it.timeReminder = EMPTY_STRING
                        displayDatePickerView()
                    }
                )
            }
            imageDatePicker.setColorFilter(color)
            textDatePicker.setTextColor(color)
            textDatePicker.text = textDate
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        if (!task?.timeReminder.isNullOrBlank()) {
            calendar.time = task?.timeReminder?.toDate() ?: Date()
        }
        context?.showDateTimePicker(calendar) {
            task?.timeReminder = it.time.formatToString()
            displayDatePickerView()
        }
    }

    private fun updateTask() {
        task?.apply {
            listId = (spinnerTaskLists.selectedItem as TaskList).id
            title = editTextTitle.text.toString()
            description = editTextDescription.text.toString()
            presenter?.updateTask(this)
        }
    }

    private fun deleteTask() {
        task?.let { presenter?.deleteTask(it.id) }
    }

    companion object {
        private const val ARG_TASK = "ARG_TASK"
        const val DETAIL_TAG = "DETAIL_FRAGMENT"

        fun newInstance(task: Task) = DetailFragment().apply {
            arguments = bundleOf(ARG_TASK to task)
        }
    }
}
