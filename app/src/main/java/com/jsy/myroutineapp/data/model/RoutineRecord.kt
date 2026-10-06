package com.jsy.myroutineapp.data.model

import java.time.LocalDate

data class RoutineRecord(
    val id : Long,
    val routineId: Long ,
    val date : LocalDate,
    val completed : Boolean
)
