package com.sunasterisk.getitdone.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    private var itemData: T? = null

    init {
        view.setOnClickListener {
            itemData?.let {
                clickItemListener(it)
            }
        }
    }

    abstract var clickItemListener: (T) -> Unit

    open fun bindData(item: T) {
        itemData = item
    }
}
