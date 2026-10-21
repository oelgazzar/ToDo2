package com.example.todo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todo.R
import com.example.todo.TodoApplication.Companion.DEFAULT_NOTIFICATION_CHANNEL_ID

fun Context.sendNotification(title: String, content: String) {
    val notification = NotificationCompat.Builder(this, DEFAULT_NOTIFICATION_CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.drawable.all_completed)
        .build()

    with(NotificationManagerCompat.from(this)) {
        if (ActivityCompat.checkSelfPermission(
                this@sendNotification,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notify(0, notification)
        }
    }
}