package com.sunasterisk.getitdone.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseActivity
import com.sunasterisk.getitdone.base.BaseFragment
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.ui.detail.DetailFragment
import com.sunasterisk.getitdone.ui.detail.DetailFragment.Companion.DETAIL_TAG
import com.sunasterisk.getitdone.ui.home.HomeFragment
import com.sunasterisk.getitdone.ui.newTask.NewTaskFragment
import com.sunasterisk.getitdone.ui.newTask.NewTaskFragment.Companion.NEW_TASK_TAG
import com.sunasterisk.getitdone.ui.newTaskList.NewTaskListFragment
import com.sunasterisk.getitdone.ui.newTaskList.NewTaskListFragment.Companion.NEW_TASK_LIST_TAG
import com.sunasterisk.getitdone.ui.taskList.TaskListFragment
import com.sunasterisk.getitdone.ui.taskList.TaskListFragment.Companion.TASK_LIST_TAG
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.addFragment
import com.sunasterisk.getitdone.utils.replaceFragment

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View,
    HomeFragment.OnItemTaskClickCallBack, TaskListFragment.OnItemTaskListClickCallBack,
    NewTaskFragment.OnNewTaskCreated, NewTaskListFragment.OnNewTaskListCreated {

    override val layoutRes get() = R.layout.activity_main
    override val styleRes get() = R.style.AppTheme
    override val presenter get() = MainPresenter()

    private var taskListId = DEFAULT_TASK_LIST_ID

    override fun initView(savedInstanceState: Bundle?) {
        initHomeFragment()
    }

    override fun onItemTaskClick(task: Task) {
        addFragment(R.id.frameContainer, DetailFragment.newInstance(task), DETAIL_TAG)
    }

    override fun onAddNewTask() {
        val addNewTaskFragment = NewTaskFragment.newInstance(taskListId)
        addNewTaskFragment.setOnNewTaskCreatedListener(this)
        addNewTaskFragment.show(supportFragmentManager, NEW_TASK_TAG)
    }

    override fun navigateTaskLists() {
        val taskListFragment = TaskListFragment.newInstance(taskListId)
        taskListFragment.setOnTaskListSelectedListener(this)
        taskListFragment.show(supportFragmentManager, TASK_LIST_TAG)
    }

    override fun openSettings() {
    }

    override fun onItemTaskListClick(id: Int) {
        taskListId = id
        initHomeFragment()
    }

    override fun onNewTaskCreated(id: Int) {
    }

    override fun onNewTaskListCreated(id: Int) {
        taskListId = id
        initHomeFragment()
    }

    override fun onAddNewTaskList() {
        val addNewTaskListFragment = NewTaskListFragment()
        addNewTaskListFragment.setOnNewTaskListCreatedListener(this)
        addFragment(R.id.frameContainer, addNewTaskListFragment, NEW_TASK_LIST_TAG)
    }

    override fun onBackPressed() {
        val fragments: List<Fragment?> = supportFragmentManager.fragments
        fragments.forEach { it?.let { if (it is BaseFragment<*, *>) it.onBackPressed() } }
        if (fragments.size == 1) super.onBackPressed()
    }

    private fun initHomeFragment() {
        val homeFragment = HomeFragment.newInstance(taskListId)
        homeFragment.setOnTaskSelectedListener(this)
        replaceFragment(R.id.frameContainer, homeFragment)
    }
}
