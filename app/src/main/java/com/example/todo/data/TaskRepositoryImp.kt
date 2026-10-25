package com.example.todo.data

import androidx.room3.withWriteTransaction
import com.example.todo.models.Reminder
import com.example.todo.models.Repeat
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import com.example.todo.schedular.ReminderSchedular
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImp @Inject constructor(
    private val taskDao: TaskDao,
    private val db: AppDatabase,
    private val schedular: ReminderSchedular
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getTasksWithCategory().map { it.toDomain() }
    }

    override fun getTask(it: Long): Flow<Task?> = taskDao.getTaskWithCategory(it).map { it?.toDomain() }

    override suspend fun upsertTask(task: Task) {
        val entity = if (task.id == 0L) { // New task, assign position at the tail
            val position = taskDao.getNextPosition()
            task.copy(position = position).toEntity()
        } else task.toEntity()

        val taskId = taskDao.upsertTask(entity)
        schedular.schedule(taskId)
    }

    override suspend fun insertAll(tasks: List<Task>) {
        taskDao.insertTasks(tasks.toEntity())
    }

    override suspend fun deleteTask(taskId: Long) = taskDao.deleteTask(taskId)

    override suspend fun updateTaskContent(
        taskId: Long,
        title: String,
        details: String
    ) = taskDao.updateTaskContent(taskId, title, details)

    override suspend fun toggleCompleted(taskId: Long) = taskDao.toggleCompleted(taskId)

    override suspend fun updateDueDate(taskId: Long, date: LocalDate?) {
        db.withWriteTransaction {
            taskDao
                .getTask(taskId)
                .first()
                ?.let {
                    val schedule = it.schedule.copy(dueDate = date)
                    taskDao.upsertTask(it.copy(schedule = schedule))
                }
        }
    }

    override suspend fun updateReminder(taskId: Long, reminder: Reminder?) {
        db.withWriteTransaction {
            taskDao
                .getTask(taskId)
                .first()
                ?.let {
                    val schedule = it.schedule.copy(
                        reminder = reminder,
                        repeat = if (reminder != null) it.schedule.repeat else null
                    )
                    taskDao.upsertTask(it.copy(schedule = schedule))
                    if (reminder != null) {
                        schedular.schedule(taskId)
                    } else {
                        schedular.cancel(taskId)
                    }
                }
        }
    }


    override suspend fun updateRepeat(taskId: Long, repeat: Repeat) {
        db.withWriteTransaction {
            taskDao
                .getTask(taskId)
                .first()
                ?.let {
                    val reminder = Reminder(
                        date = repeat.startDate ?: LocalDate.now(),
                        time = repeat.time
                    )
                    val schedule = Schedule(reminder = reminder, repeat = repeat)
                    taskDao.upsertTask(it.copy(schedule = schedule))
                    schedular.schedule(taskId)
                }
        }
    }

    override suspend fun removeRepeat(taskId: Long) {
        db.withWriteTransaction {
            taskDao
                .getTask(taskId)
                .first()
                ?.let {
                    val schedule = it.schedule.copy(repeat = null)
                    taskDao.upsertTask(it.copy(schedule = schedule))
                }
        }
    }

    override suspend fun updatePositions(tasks: List<Task>) {
        db.withWriteTransaction {
            tasks.forEachIndexed { index, task ->
                taskDao.updatePosition(task.id, index.toLong())
            }
        }
    }

    override suspend fun updateCategory(taskId: Long, categoryId: Long?) {
        taskDao.updateCategory(taskId, categoryId)
    }
}