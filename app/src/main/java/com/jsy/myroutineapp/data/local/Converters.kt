package com.jsy.myroutineapp.data.local

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    // Set<DayOfWeek> <-> "MONDAY,WEDNESDAY"
    @TypeConverter
    fun fromDays(days: Set<DayOfWeek>): String = days.sorted().joinToString(",") { it.name }

    @TypeConverter
    fun toDays(value: String): Set<DayOfWeek> =
        if (value.isEmpty()) emptySet() else value.split(",").map { DayOfWeek.valueOf(it) }.toSet()
}
