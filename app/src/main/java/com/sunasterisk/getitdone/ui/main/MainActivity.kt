package com.sunasterisk.getitdone.ui.main

import android.os.Bundle
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.base.BaseActivity

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View {

    override val layoutRes: Int
        get() = R.layout.activity_main
    override val styleRes: Int
        get() = R.style.AppTheme
    override val presenter: MainPresenter?
        get() = MainPresenter()

    override fun initView(savedInstanceState: Bundle?) {
        
    }
}
