package com.jsy.myroutineapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord

@Database(
    entities = [Routine::class, RoutineRecord::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE routines ADD COLUMN groupId TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE routines ADD COLUMN endDate INTEGER")
        db.execSQL("UPDATE routines SET groupId = 'legacy-' || id")   // 기존 루틴마다 서로 다른 그룹
        db.execSQL("CREATE INDEX IF NOT EXISTS index_routines_groupId ON routines(groupId)")
    }
}