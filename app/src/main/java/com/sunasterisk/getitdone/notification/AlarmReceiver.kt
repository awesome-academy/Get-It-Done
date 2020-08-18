package com.sunasterisk.getitdone.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sunasterisk.getitdone.R
import com.sunasterisk.getitdone.data.model.Task
import com.sunasterisk.getitdone.ui.main.MainActivity
import com.sunasterisk.getitdone.utils.Constants
import com.sunasterisk.getitdone.utils.Notification.CHANNEL_ID
import com.sunasterisk.getitdone.utils.ParcelableUtils

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val task = intent.getByteArrayExtra(Constants.EXTRA_ITEM_BYTE_ARRAY)?.let {
            ParcelableUtils.toParcelable(it, Task.CREATOR)
        }
        createNotificationChannel(context)

        val intentClickNotification = Intent(context, MainActivity::class.java).apply {
            action = Constants.ACTION_GO_TO_TASK_DETAIL_FROM_NOTIFICATION
            putExtra(Constants.EXTRA_ITEM, task)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intentClickNotification, 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(task?.title)
            .setContentText(task?.description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            task?.id?.let { notify(it, builder.build()) }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
