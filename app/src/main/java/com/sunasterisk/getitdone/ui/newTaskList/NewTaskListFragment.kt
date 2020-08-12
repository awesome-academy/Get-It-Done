package com.sunasterisk.getitdone.ui.newTaskList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.ui.detail.DetailFragment

class NewTaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task_list, container, false)
    }
    
    companion object {
        const val NEW_TASK_LIST_TAG = "TASK_LIST_FRAGMENT"
    }
}
