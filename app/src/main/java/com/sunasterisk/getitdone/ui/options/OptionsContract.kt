package com.sunasterisk.getitdone.ui.options

import android.os.Bundle
import com.sunasterisk.getitdone.base.BaseContract

interface OptionsContract {
    interface View : BaseContract.View {
        fun displayMessage(message: String)
        fun displayMessage(stringId: Int)
        fun sendRequest(requestKey: String)
        fun onDeleteListSuccessful()
        fun dismissFragment()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun deleteTaskList(id: Int)
        fun deleteAllCompletedTask(id: Int)
    }
}
