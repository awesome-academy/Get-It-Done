package com.sunasterisk.getitdone.data

import java.lang.Exception

interface OnLoadedCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(exception: Exception)
}
