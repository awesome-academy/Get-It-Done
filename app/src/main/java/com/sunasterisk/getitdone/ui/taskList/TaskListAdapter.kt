package com.sunasterisk.getitdone.ui.taskList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseAdapter
import com.sunasterisk.getitdone.base.BaseViewHolder
import com.sunasterisk.getitdone.data.model.TaskList
import kotlinx.android.synthetic.main.task_list_item.view.*

class TaskListAdapter : BaseAdapter<TaskList, TaskListAdapter.TaskListViewHolder>() {
    var selectedId = -1
    override var items = mutableListOf<TaskList>()
    override var clickItemListener: (TaskList) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskListViewHolder(
        selectedId,
        LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false),
        clickItemListener
    )

    class TaskListViewHolder(
        private var selectedTaskListId: Int,
        itemView: View,
        override var clickItemListener: (TaskList) -> Unit
    ) : BaseViewHolder<TaskList>(itemView) {
        override fun bindData(item: TaskList) {
            super.bindData(item)
            itemView.textTaskListTitle.text = item.title
            if (item.id == selectedTaskListId) itemView.setBackgroundColor(Color.LTGRAY)
        }
    }
}
