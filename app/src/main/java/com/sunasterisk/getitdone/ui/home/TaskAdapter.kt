package com.sunasterisk.getitdone.ui.home

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseAdapter
import com.sunasterisk.getitdone.base.BaseViewHolder
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.utils.Constants.STATUS_NOT_COMPLETE
import com.sunasterisk.getitdone.utils.dateDiff
import com.sunasterisk.getitdone.utils.toDate
import kotlinx.android.synthetic.main.task_item.view.*
import java.util.*

class TaskAdapter : BaseAdapter<Task, TaskAdapter.TaskViewHolder>() {

    override var items = mutableListOf<Task>()
    var onCheckboxClickListener: (Task) -> Unit = { _ -> }
    var onImportantClickListener: (Task) -> Unit = { _ -> }
    override var clickItemListener: (Task) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false),
            onCheckboxClickListener, onImportantClickListener, clickItemListener
        )

    class TaskViewHolder(
        itemView: View,
        var onCheckboxClickListener: (Task) -> Unit,
        var onImportantClickListener: (Task) -> Unit,
        override var clickItemListener: (Task) -> Unit
    ) : BaseViewHolder<Task>(itemView) {

        override fun bindData(item: Task) {
            super.bindData(item)
            itemView.textTitle.text = item.title
            displayTaskDescription(item)
            displayTaskStatus(item)
            displayTaskReminder(item)
            displayTaskImportant(item)
        }

        private fun displayTaskDescription(task: Task) {
            itemView.textDescription.apply {
                if (task.description.isNotBlank()) {
                    visibility = View.VISIBLE
                    text = task.description
                } else
                    visibility = View.GONE
            }
        }

        private fun displayTaskStatus(task: Task) {
            itemView.apply {
                if (task.status == STATUS_NOT_COMPLETE) {
                    imageStatus.setImageResource(R.drawable.ic_not_check)
                } else {
                    imageStatus.setImageResource(R.drawable.ic_checked)
                    textTitle.paintFlags =
                        textTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }

        private fun displayTaskReminder(task: Task) {
            itemView.textReminder.apply {
                if (task.timeReminder.isNotBlank() && task.status == STATUS_NOT_COMPLETE) {
                    visibility = View.VISIBLE
                    val dateReminder = task.timeReminder.toDate() ?: Date()
                    val dayDiff = Date().dateDiff(dateReminder)
                    when {
                        dayDiff < 0 -> {
                            displayFutureTasks(task, dayDiff)
                        }
                        dayDiff > 0 -> {
                            displayPastTasks(dayDiff)
                        }
                        else -> {
                            setTextColor(Color.BLUE)
                            text = context.getString(R.string.text_today)
                        }
                    }
                } else
                    visibility = View.GONE
            }
        }

        private fun displayFutureTasks(task: Task, dayDiff: Int) {
            itemView.textReminder.apply {
                setTextColor(Color.DKGRAY)
                text =
                    if (dayDiff == -1) context.getString(R.string.text_tomorrow) else task.timeReminder
            }
        }

        private fun displayPastTasks(dayDiff: Int) {
            itemView.textReminder.apply {
                setTextColor(Color.RED)
                val dayDiffString =
                    when (dayDiff) {
                        1 -> context.getString(R.string.text_yesterday)
                        in 2..6 -> "$dayDiff ${context.getString(R.string.text_days_ago)}"
                        7 -> context.getString(R.string.text_one_week_ago)
                        else -> "${dayDiff / 7} ${context.getString(R.string.text_weeks_ago)}"
                    }
                text = dayDiffString
            }
        }

        private fun displayTaskImportant(task: Task) {
            itemView.apply {
                if (task.isImportant)
                    btnImportant.setImageResource(R.drawable.ic_star_on)
                else
                    btnImportant.setImageResource(R.drawable.ic_star_off)

                imageStatus.setOnClickListener { onCheckboxClickListener(task) }
                btnImportant.setOnClickListener { onImportantClickListener(task) }
            }
        }
    }
}
