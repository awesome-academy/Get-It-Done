package com.sunasterisk.getitdone.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseFragment
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.utils.toast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeContract.View, HomePresenter>(), HomeContract.View {

    override val layoutId get() = R.layout.fragment_home
    override val presenter get() = HomePresenter.getInstance()

    private var listId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { listId = it.getInt(ARG_LIST_ID) }
        (activity as AppCompatActivity).setSupportActionBar(toolbarHome)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initComponents(savedInstanceState: Bundle?) {
        presenter.getTaskListFromId(id = listId)
        presenter.getTasksFromTaskListId(listId = listId)
    }

    override fun getParentContext() = this.requireContext()

    override fun setToolBarTitle(title: String) {
        toolbarHome.title = title
    }

    override fun displayMessage(message: String) {
        requireContext().toast(message)
    }

    override fun handleLoadedTasks(tasks: List<Task>) {
    }

    override fun handleException(exception: Exception) {
    }

    companion object {
        private const val ARG_LIST_ID = "LIST_ID"
        const val HOME_TAG = "HOME_FRAGMENT"

        fun newInstance(listId: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LIST_ID, listId)
                }
            }
    }
}
