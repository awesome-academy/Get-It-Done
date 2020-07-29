package com.sunasterisk.getitdone.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>> :
        Fragment(), BaseContract.View {

    @get:LayoutRes
    protected abstract val layoutId: Int

    abstract val presenter: P?

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.attach(this as V)
        initComponents(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        
        presenter?.detach()
    }
    
    protected abstract fun initComponents(savedInstanceState: Bundle?)
}
