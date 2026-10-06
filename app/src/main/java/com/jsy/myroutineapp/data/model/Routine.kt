package com.jsy.myroutineapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: RoutineCategory,
    val repeatDays: Set<DayOfWeek>,
    val startDate: LocalDate
)
