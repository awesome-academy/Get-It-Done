package com.sunasterisk.getitdone.base

interface BaseContract {
    interface Presenter<in T> {
        fun attach(view: T)
        fun detach()
    }

    interface View {
    }
}
