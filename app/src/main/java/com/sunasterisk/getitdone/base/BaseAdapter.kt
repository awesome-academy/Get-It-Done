package com.sunasterisk.getitdone.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, V : BaseViewHolder<T>> : RecyclerView.Adapter<V>() {

    abstract var items: MutableList<T>

    abstract var clickItemListener: (T) -> Unit

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: V, position: Int) {
        viewHolder.bindData(items[position])
    }

    open fun loadItems(newItems: MutableList<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun insertItem(newItem: T) {
        items.add(newItem)
        notifyItemInserted(items.indexOf(newItem))
    }

    fun removeItem(item: T) {
        val position = items.indexOf(item)
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItemAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(item: T) {
        notifyItemChanged(items.indexOf(item))
    }
    
    fun removeAllItems(){
        items.clear()
        notifyDataSetChanged()
    }
}
