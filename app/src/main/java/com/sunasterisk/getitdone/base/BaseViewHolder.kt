package com.sunasterisk.getitdone.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener { clickItemListener(adapterPosition) }
    }

    abstract var clickItemListener: (Int) -> Unit

    open fun bindData(item: T) {}
}
