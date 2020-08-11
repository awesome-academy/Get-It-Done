package com.sunasterisk.getitdone.ui.newTaskList

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.appcompat.app.AppCompatActivity
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseFragment
import com.sunasterisk.getitdone.data.model.TaskList
import com.sunasterisk.getitdone.data.repository.TaskListRepository
import com.sunasterisk.getitdone.data.source.local.TaskListLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskListDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.popFragment
import com.sunasterisk.getitdone.utils.toast
import kotlinx.android.synthetic.main.fragment_new_task_list.*

class NewTaskListFragment : BaseFragment<NewTaskListContract.View, NewTaskListPresenter>(),
    NewTaskListContract.View {

    override val layoutId get() = R.layout.fragment_new_task_list

    private var presenter: NewTaskListPresenter? = null

    private var callback: OnNewTaskListCreated? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.slide_bottom)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        iniPresenter()
        initListener()
    }

    override fun onNewTaskListCreated(id: Long) {
        callback?.onNewTaskListCreated(id.toInt())
    }

    override fun displayMessage(message: String) {
        context?.run { toast(message) }
    }

    override fun displayMessage(stringId: Int) {
        context?.run { toast(getString(stringId)) }
    }

    override fun popFragment() {
        onBackPressed()
    }

    override fun onBackPressed() {
        (context as AppCompatActivity).popFragment()
    }

    fun setOnNewTaskListCreatedListener(callback: OnNewTaskListCreated) {
        this.callback = callback
    }

    private fun iniPresenter(){
        context?.let {
            val database = AppDatabase.getInstance(it)

            val taskListRepository = TaskListRepository.getInstance(
                TaskListLocalDataSource.getInstance(
                    TaskListDAOImpl.getInstance(database)
                )
            )

            presenter = NewTaskListPresenter(taskListRepository).apply {
                attach(this@NewTaskListFragment)
            }
        }
    }
    
    private fun initListener(){
        btnClose.setOnClickListener { onBackPressed() }
        btnDone.setOnClickListener { onSaveTaskList() }
    }
    
    private fun onSaveTaskList() {
        val inputTextTitle = editTextTitle.text.toString()
        if (inputTextTitle.isBlank()) {
            context?.run { toast(getString(R.string.msg_title_empty)) }
        } else {
            presenter?.addNewTaskList(TaskList(title = inputTextTitle))
        }
    }

    interface OnNewTaskListCreated {
        fun onNewTaskListCreated(id: Int)
    }

    companion object {
        const val NEW_TASK_LIST_TAG = "TASK_LIST_FRAGMENT"
    }
}
