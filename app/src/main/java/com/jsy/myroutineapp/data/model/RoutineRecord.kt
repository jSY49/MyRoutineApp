package com.jsy.myroutineapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "routine_records",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // 같은 루틴의 같은 날짜 기록은 하나만
    indices = [Index(value = ["routineId", "date"], unique = true)]
)
data class RoutineRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val routineId: Long,
    val date: LocalDate,
)
//완료된 작업만 디비에 저장
