package com.sunasterisk.getitdone.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.notification.AlarmReceiver
import com.sunasterisk.getitdone.utils.Constants.EXTRA_ITEM_BYTE_ARRAY

fun setUpAlarm(context: Context?, task: Task) {
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtras(bundleOf(EXTRA_ITEM_BYTE_ARRAY to ParcelableUtils.toByteArray(task)))
    }
    val pendingIntent =
        PendingIntent.getBroadcast(
            context.applicationContext,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    task.timeReminder.toDate()?.let {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, it.time, pendingIntent)
    }
}

fun cancelAlarm(context: Context?, vararg tasks: Task) {
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    for (task in tasks) {
        val pendingIntent =
            PendingIntent.getBroadcast(context, task.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }
}
