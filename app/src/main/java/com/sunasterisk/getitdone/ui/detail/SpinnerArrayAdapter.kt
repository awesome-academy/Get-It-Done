package com.sunasterisk.getitdone.ui.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.sunasterisk.getitdone.data.model.TaskList

class SpinnerArrayAdapter(context: Context, items: List<TaskList>) :
    ArrayAdapter<TaskList>(context, android.R.layout.simple_spinner_dropdown_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        view.setPadding(0, view.paddingTop, view.paddingRight, view.paddingBottom)
        return view
    }
}
