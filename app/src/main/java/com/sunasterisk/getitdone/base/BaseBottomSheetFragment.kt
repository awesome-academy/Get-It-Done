package com.sunasterisk.getitdone.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<V : BaseContract.View, P : BaseContract.Presenter<V>> :
    BottomSheetDialogFragment(), BaseContract.View {

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(savedInstanceState)
    }

    protected abstract fun initComponents(savedInstanceState: Bundle?)
}
