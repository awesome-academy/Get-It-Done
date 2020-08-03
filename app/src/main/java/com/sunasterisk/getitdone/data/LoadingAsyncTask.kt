package com.sunasterisk.getitdone.data

import android.os.AsyncTask

class LoadingAsyncTask<P, R>(
    private val callback: OnLoadedCallback<R>,
    private val handler: (P) -> R
) : AsyncTask<P, Void, R?>() {

    override fun doInBackground(vararg params: P): R? =
        try {
            handler(params[0])
        } catch (e: Exception) {
            null
        }

    override fun onPostExecute(result: R?) {
        super.onPostExecute(result)

        result?.let {
            callback.onSuccess(it)
        } ?: callback.onFailure(Exception("LoadingAsyncTask Exception"))
    }
}
