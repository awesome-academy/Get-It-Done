package com.sunasterisk.getitdone.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> :
    AppCompatActivity(), BaseContract.View {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    @get:StyleRes
    protected abstract val styleRes: Int

    abstract val presenter: P?

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(styleRes)
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        presenter?.attach(this as V)
        initView(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.detach()
    }

    abstract fun initView(savedInstanceState: Bundle?)
}
