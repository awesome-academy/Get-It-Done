package com.sunasterisk.getitdone.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseAdapter
import com.sunasterisk.getitdone.base.BaseViewHolder
import kotlinx.android.synthetic.main.task_item_header.view.*

class CompletedTaskTitleAdapter : BaseAdapter<Int, CompletedTaskTitleAdapter.HeaderViewHolder>() {

    override var items = mutableListOf<Int>()
    override var clickItemListener: (Int) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item_header, parent, false)
        ) {}

    class HeaderViewHolder(itemView: View, override var clickItemListener: (Int) -> Unit) :
        BaseViewHolder<Int>(itemView) {
        override fun bindData(item: Int) {
            super.bindData(item)
            if (item != 0) {
                itemView.layout_item_header.visibility = View.VISIBLE
                itemView.text_completed_task_title.text = "Completed tasks ($item)"
            } else {
                itemView.layout_item_header.visibility = View.GONE
            }
        }
    }
}
