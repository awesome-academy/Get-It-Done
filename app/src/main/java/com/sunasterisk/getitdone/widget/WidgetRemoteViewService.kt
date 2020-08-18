package com.sunasterisk.getitdone.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetRemoteViewService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        WidgetRemoteViewFactory(applicationContext, intent)
}
