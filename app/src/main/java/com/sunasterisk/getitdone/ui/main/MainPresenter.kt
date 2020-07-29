package com.sunasterisk.getitdone.ui.main

class MainPresenter : MainContract.Presenter {

    private var view: MainContract.View? = null

    override fun attach(view: MainContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
    }
}
