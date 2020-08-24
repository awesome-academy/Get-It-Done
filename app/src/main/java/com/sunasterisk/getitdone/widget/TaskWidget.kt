package com.sunasterisk.getitdone.widget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.ui.main.MainActivity
import com.sunasterisk.getitdone.utils.Constants.ACTION_ADD_NEW_TASK
import com.sunasterisk.getitdone.utils.Constants.DEFAULT_TASK_LIST_ID

class TaskWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val appWidgetManager = AppWidgetManager.getInstance(it)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(it, this::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listWidgetItems)
            if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
                for (appWidgetId in appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId)
                }
            }
        }
        super.onReceive(context, intent)
    }


}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.task_widget)

    val intent = Intent(context, MainActivity::class.java).apply {
        action = ACTION_ADD_NEW_TASK
    }
    val taskStackBuilder = TaskStackBuilder.create(context).apply {
        addParentStack(MainActivity::class.java)
        addNextIntent(intent)
    }
    val addTaskPendingIntent =
        taskStackBuilder.getPendingIntent(DEFAULT_TASK_LIST_ID, PendingIntent.FLAG_UPDATE_CURRENT)

    val itemClickIntent = Intent(context, MainActivity::class.java)

    val itemClickPendingIntent = TaskStackBuilder.create(context)
        .addNextIntentWithParentStack(itemClickIntent)
        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

    val serviceIntent = Intent(context, WidgetRemoteViewService::class.java).apply {
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        data = Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME))
    }

    views.apply {
        setOnClickPendingIntent(R.id.buttonWidgetAddTask, addTaskPendingIntent)
        setTextViewText(R.id.textWidgetTaskListTitle, "My day")
        setRemoteAdapter(R.id.listWidgetItems, serviceIntent)
        setEmptyView(R.id.listWidgetItems, R.id.textWidgetEmptyList)
        setPendingIntentTemplate(R.id.listWidgetItems, itemClickPendingIntent)
    }

    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listWidgetItems)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
