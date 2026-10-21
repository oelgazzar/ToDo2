package com.example.todo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        getSystemService(NotificationManager::class.java)?.run {
            createNotificationChannel(
                NotificationChannel(
                    DEFAULT_NOTIFICATION_CHANNEL_ID,
                    "Todo",
                    NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
    }

    companion object {
        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "default"
    }
}