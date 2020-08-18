package com.sunasterisk.getitdone.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.OnLoadedCallback
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.data.repository.TaskRepository
import com.sunasterisk.getitdone.data.source.local.TaskLocalDataSource
import com.sunasterisk.getitdone.data.source.local.dao.TaskDAOImpl
import com.sunasterisk.getitdone.data.source.local.database.AppDatabase
import com.sunasterisk.getitdone.utils.Constants
import com.sunasterisk.getitdone.utils.formatToString
import com.sunasterisk.getitdone.utils.toast
import java.util.*
import kotlin.collections.ArrayList


class WidgetRemoteViewFactory(private val context: Context, private val intent: Intent?) :
    RemoteViewsService.RemoteViewsFactory {

    private var items = arrayListOf<Task>()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate() {
        updateList()
    }

    override fun getLoadingView() = RemoteViews(context.packageName, R.layout.task_widget)

    override fun onDataSetChanged() {
        updateList()
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun hasStableIds() = true

    override fun getViewAt(position: Int): RemoteViews {
        val fillIntent = Intent().apply {
            action = Constants.ACTION_GO_TO_TASK_DETAIL
            putExtra(Constants.EXTRA_ITEM, items[position])
        }
        val remoteView = RemoteViews(context.packageName, R.layout.task_widget_item)

        remoteView.apply {
            setViewVisibility(R.id.checkBoxWidgetItemDefault, View.GONE)
            setViewVisibility(R.id.checkBoxWidgetItemGreen, View.GONE)
            setViewVisibility(R.id.checkBoxWidgetItemCyan, View.GONE)
            setViewVisibility(R.id.checkBoxWidgetItemLime, View.GONE)
            setViewVisibility(R.id.checkBoxWidgetItemOrange, View.GONE)
            setViewVisibility(R.id.checkBoxWidgetItemRed, View.GONE)
            setViewVisibility(R.id.checkBoxWidgetItemPurple, View.GONE)

            val itemColorId = when (items[position].color) {
                Constants.COLOR_GREEN -> R.id.checkBoxWidgetItemGreen
                Constants.COLOR_CYAN -> R.id.checkBoxWidgetItemCyan
                Constants.COLOR_LIME -> R.id.checkBoxWidgetItemLime
                Constants.COLOR_ORANGE -> R.id.checkBoxWidgetItemOrange
                Constants.COLOR_RED -> R.id.checkBoxWidgetItemRed
                Constants.COLOR_PURPLE -> R.id.checkBoxWidgetItemPurple
                else -> R.id.checkBoxWidgetItemDefault
            }
            remoteView.setViewVisibility(itemColorId, View.VISIBLE)

            when (items[position].timeReminder) {
                Constants.EMPTY_STRING -> setViewVisibility(
                    R.id.textWidgetItemReminder,
                    View.GONE
                )
                else -> {
                    setViewVisibility(R.id.textWidgetItemReminder, View.VISIBLE)
                }
            }

            when (items[position].description) {
                Constants.EMPTY_STRING -> setViewVisibility(
                    R.id.textWidgetItemDescription,
                    View.GONE
                )
                else -> {
                    setViewVisibility(R.id.textWidgetItemDescription, View.VISIBLE)
                }
            }

            setTextViewText(R.id.textWidgetItemTitle, items[position].title)
            setTextViewText(R.id.textWidgetItemReminder, items[position].timeReminder)
            setOnClickFillInIntent(R.id.layoutWidgetItem, fillIntent)
        }

        return remoteView
    }

    override fun getCount() = items.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {
        items.clear()
    }

    private fun updateList() {
        intent?.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )?.let { this.appWidgetId = it }

        val taskRepository = TaskRepository.getInstance(
            TaskLocalDataSource.getInstance(
                TaskDAOImpl.getInstance(
                    AppDatabase.getInstance(context)
                )
            )
        )
        val todayString = Date().formatToString(Constants.DAY_FORMAT)
        taskRepository.getTaskInMyDay(todayString, object : OnLoadedCallback<List<Task>> {
            override fun onSuccess(data: List<Task>) {
                items.clear()
                items.addAll(data.filter { it.status == Constants.STATUS_NOT_COMPLETE })
                onDataSetChanged()
            }

            override fun onFailure(exception: Exception) {
                context.toast(exception.message.toString())
            }
        })
    }
}
