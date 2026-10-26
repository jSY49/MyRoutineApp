package com.jsy.myroutineapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.jsy.myroutineapp.notification.NotificationChannels.ROUTINE
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RoutineApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
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