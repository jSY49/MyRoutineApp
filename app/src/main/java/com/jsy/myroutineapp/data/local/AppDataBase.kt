package com.jsy.myroutineapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord

@Database(
    entities = [Routine::class, RoutineRecord::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}