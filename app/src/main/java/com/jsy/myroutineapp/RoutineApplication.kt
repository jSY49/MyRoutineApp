package com.jsy.myroutineapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.jsy.myroutineapp.notification.NotificationChannels.ROUTINE
import com.jsy.myroutineapp.notification.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RoutineApplication : Application(){

    @Inject lateinit var reminderScheduler: ReminderScheduler

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        reminderScheduler.schedule()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                ROUTINE,
                getString(R.string.Notification_channel_routine_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.Notification_channel_routine_desc)
            }
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}