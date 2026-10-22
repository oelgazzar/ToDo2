package com.example.todo.schedular

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {
    @Inject
    lateinit var schedular: ReminderSchedular

    override fun onReceive(context: Context?, intent: Intent?) {
        val pending = goAsync()
        CoroutineScope(Job()).launch {
            schedular.rescheduleAll()
        }
        pending.finish()
    }
}