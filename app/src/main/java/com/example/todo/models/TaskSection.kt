package com.example.todo.models

import com.example.todo.ui.utils.isPast
import com.example.todo.ui.utils.toDisplayText
import com.example.todo.ui.utils.toDueBucket
import com.example.todo.ui.utils.toReminderBucket
import java.time.LocalDate
import java.time.LocalDateTime

data class TaskSection(
    val header: String? = null,
    val items: List<Task>
)

fun List<Task>.toTaskSections(sortType: SortType): List<TaskSection> {
    return when (sortType) {
        SortType.CUSTOM -> listOf(
            TaskSection(
                items = this
            )
        )
        SortType.TITLE -> listOf(
            TaskSection(
                items = this.sortedBy { it.title }
            )
        )
        SortType.DEADLINE -> {
            this.groupBy {
                val dueDate = it.schedule.dueDate
                if (it.schedule.dueDate?.isPast == true) {
                    "Overdue"
                } else {
                    dueDate?.toDisplayText()
                }
            }.map { (dueBucket, tasks) ->
                TaskSection(
                    header = dueBucket,
                    items = tasks
                )
            }.sortedWith(compareBy(nullsLast(), {it.items.first().schedule.dueDate}))
                .map { section ->
                    section.header?.let { section }?:section.copy(header = "No deadline")
                }
        }

        SortType.REMINDER -> {
            this.groupBy {
                val reminder = it.schedule.reminder?.date
                if (it.schedule.reminder?.date?.isPast == true) {
                    "Past"
                } else {
                    reminder?.toDisplayText()
                }
            }.map { (reminderBucket, tasks) ->
                TaskSection(
                    header = reminderBucket,
                    items = tasks
                )
            }.sortedWith(compareBy(nullsLast(), {it.items.first().schedule.reminder?.date}))
                .map { section ->
                    section.header?.let { section }?:section.copy(header = "No reminders")
                }
        }

        SortType.CATEGORY -> {
            this.groupBy { it.category?.name }
                .map { (category, tasks) ->
                    TaskSection(
                        header = category,
                        items = tasks
                    )
                }.sortedWith(compareBy(nullsLast(), { it.header }))
                .map { section ->
                    section.header?.let { section }?:section.copy(header = "No category")
                }
        }
    }
}
