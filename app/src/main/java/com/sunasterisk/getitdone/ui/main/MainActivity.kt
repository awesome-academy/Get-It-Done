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
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID
import com.sunasterisk.getitdone.utils.addFragment
import com.sunasterisk.getitdone.utils.popFragment

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View,
    HomeFragment.OnItemTaskClickCallBack {

    override val layoutRes: Int
        get() = R.layout.activity_main
    override val styleRes: Int
        get() = R.style.AppTheme
    override val presenter: MainPresenter
        get() = MainPresenter()

    override fun initView(savedInstanceState: Bundle?) {
        val homeFragment = HomeFragment.newInstance(DEFAULT_TASK_LIST_ID)
        homeFragment.setOnTaskSelectedListener(this)
        addFragment(R.id.frameContainer, homeFragment)
    }

    override fun onItemTaskClick(task: Task) {
        addFragment(R.id.frameContainer, DetailFragment.newInstance(task), DETAIL_TAG)
    }
    
    override fun onBackPressed() {
        val fragments: List<Fragment?> = supportFragmentManager.fragments
        fragments.filterIsInstance<BaseFragment<*, *>>().forEach {
            it.onBackPressed()
        }
        if (fragments.size == 1)
            super.onBackPressed()
    }
}
