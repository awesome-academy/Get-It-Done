package com.sunasterisk.getitdone.ui.main

import android.os.Bundle
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseActivity
import com.sunasterisk.getitdone.ui.home.HomeFragment
import com.sunasterisk.getitdone.ui.home.HomeFragment.Companion.HOME_TAG
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View {

    override val layoutRes: Int
        get() = R.layout.activity_main
    override val styleRes: Int
        get() = R.style.AppTheme
    override val presenter: MainPresenter
        get() = MainPresenter()
    
    override fun initView(savedInstanceState: Bundle?) {
        addFragment(R.id.container, HomeFragment.newInstance(DEFAULT_TASK_LIST_ID), HOME_TAG)
    }
}
