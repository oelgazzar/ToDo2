package com.example.todo.schedular

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todo.utils.sendNotification

class ReminderReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")?:return
        val content = intent.getStringExtra("content")?:return

        context?.sendNotification(title, content)
    }

}