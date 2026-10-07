package com.jsy.myroutineapp

import android.app.Application
import androidx.room.Room
import com.jsy.myroutineapp.data.local.AppDatabase

class RoutineApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "routine.db"
        )
            .build()
    }
}