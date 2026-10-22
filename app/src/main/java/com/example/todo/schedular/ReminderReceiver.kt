package com.example.todo.schedular

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todo.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver: BroadcastReceiver() {
    @Inject
    lateinit var schedular: ReminderSchedular

    override fun onReceive(context: Context?, intent: Intent?) {
        val taskId = intent?.getLongExtra("taskId", -1L)?:return
        val title = intent.getStringExtra("title")?:""
        val content = intent.getStringExtra("content")?:""
        val pending = goAsync()
        CoroutineScope(Job()).launch {
            context?.sendNotification(title, content)
            schedular.scheduleNext(taskId)
        }
        pending.finish()
    }
}