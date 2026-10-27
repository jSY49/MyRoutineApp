package com.jsy.myroutineapp.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "routines",
    indices = [Index("groupId")])   //그룹으로 자주 조회할 예정
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: RoutineCategory,
    val repeatDays: Set<DayOfWeek>,
    val description : String,
    val startDate: LocalDate,
    val groupId : String = UUID.randomUUID().toString(),
    val endDate : LocalDate? = null
)

// 그 날짜에 수행해야 하는 루틴인지
fun Routine.isScheduledOn(date: LocalDate) =
    !date.isBefore(startDate) &&
            (endDate == null || !date.isAfter(endDate)) &&   // 종료된 줄은 종료일 이후 제외
            date.dayOfWeek in repeatDays