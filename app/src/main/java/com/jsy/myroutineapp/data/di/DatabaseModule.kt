package com.jsy.myroutineapp.data.di

import android.content.Context
import androidx.room.Room
import com.jsy.myroutineapp.data.local.AppDatabase
import com.jsy.myroutineapp.data.local.MIGRATION_1_2
import com.jsy.myroutineapp.data.local.RoutineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "routine.db"
        )
            .addMigrations(MIGRATION_1_2) //마이그레이션 시
            .build()
    }

    @Provides
    fun providesRoutineDao(appDataBase: AppDatabase): RoutineDao {
        return appDataBase.routineDao()
    }
}