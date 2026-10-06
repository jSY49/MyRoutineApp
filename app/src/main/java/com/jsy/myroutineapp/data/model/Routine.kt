package com.jsy.myroutineapp.data.model

import java.time.DayOfWeek
import java.time.LocalDate

data class Routine(
    val id : Long,
    val title : String,
    val category: RoutineCategory,
    val repeatDays :Set<DayOfWeek>,
    val startDate : LocalDate
)
