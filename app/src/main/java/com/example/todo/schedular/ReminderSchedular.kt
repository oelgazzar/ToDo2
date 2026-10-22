package com.example.todo.schedular

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todo.data.TaskDao
import com.example.todo.models.toLocalDateTime
import com.example.todo.models.toReminder
import com.example.todo.models.utils.nextOccurrence
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Singleton
class ReminderSchedular @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationContext private val context: Context
) {
    private val alarmManager by lazy {
        context.getSystemService(AlarmManager::class.java) as AlarmManager
    }

    suspend fun schedule(taskId: Long) {
        val task = taskDao.getTaskOnce(taskId) ?: return
        val reminder = task.schedule.reminder
        reminder ?: return
        println("Will schedule reminder for ${reminder.toLocalDateTime()}")

        val time = reminder.time ?: LocalTime.now()
            .takeIf {
                it.isAfter(LocalTime.of(9, 0))
            } ?: LocalTime.of(9, 0)

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminder.date
                .atTime(time)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            PendingIntent.getBroadcast(
                context,
                taskId.toInt(),
                Intent(context, ReminderReceiver::class.java).apply {
                    putExtra("taskId", task.id)
                    putExtra("title", task.title)
                    putExtra("content", task.details)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(taskId: Long) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                taskId.toInt(),
                Intent(context, ReminderReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    suspend fun scheduleNext(taskId: Long) {
        val task = taskDao.getTaskOnce(taskId) ?: return
        val repeat = task.schedule.repeat ?: return

        val nextReminder = repeat.nextOccurrence()?.toReminder()
        taskDao.upsertTask(task.copy(schedule = task.schedule.copy(reminder = nextReminder)))
        if (nextReminder != null) {
            schedule(taskId)
        }
    }

    suspend fun rescheduleAll() {
        val tasks = taskDao.getAllTasks().first()
        tasks.forEach { taskEntity ->
            if (taskEntity.schedule.reminder != null &&
                !taskEntity.schedule.reminder.toLocalDateTime().isBefore(LocalDateTime.now()) &&
                !taskEntity.completed
            ) {
                schedule(taskEntity.id)
            }
        }
    }
}