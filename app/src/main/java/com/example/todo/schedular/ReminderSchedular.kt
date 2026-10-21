package com.example.todo.schedular

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todo.data.TaskDao
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.LocalTime
import java.time.ZoneId

@Singleton
class ReminderSchedular @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationContext private val context: Context
) {
    suspend fun schedule(taskId: Long) {
        val task = taskDao.getTaskOnce(taskId) ?: return
        val reminder = task.schedule.reminder
        reminder ?: return

        val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager

        val time = reminder.time ?: LocalTime.now()
            .takeIf {
                it.isAfter(LocalTime.of(9, 0))
            } ?: LocalTime.of(9, 0)

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminder.date
                .atTime(reminder.time ?: LocalTime.now())
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            PendingIntent.getBroadcast(
                context,
                taskId.toInt(),
                Intent(context, ReminderReceiver::class.java).apply {
                    putExtra("title", task.title)
                    putExtra("content", task.details)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}