package com.jsy.myroutineapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RoutineDao {

    //루틴
    @Query("SELECT * FROM routines")
    fun observeAllRoutines() : Flow<List<Routine>> // 루틴 추가 시 자동 갱신

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoutine(routine: Routine) : Long

    @Query("DELETE FROM routines WHERE id = :routineId")
    suspend fun deleteRoutine(routineId: Long)

    @Update
    suspend fun updateRoutine(routine : Routine)

    //루틴 기록
    @Query("SELECT * FROM routine_records")
    fun observeAllRoutineRecords() : Flow<List<RoutineRecord>>

    @Query("SELECT * FROM routine_records WHERE date BETWEEN :from AND :to")
    fun observeRangeRoutineRecords(from: LocalDate, to: LocalDate): Flow<List<RoutineRecord>>

    @Query(
        "SELECT * FROM routine_records " +
                "WHERE routineId = :routineId AND date BETWEEN :from AND :to"
    )
    fun observeRangeSpecialRoutineRecord(
        routineId: Long,
        from: LocalDate,
        to: LocalDate
    ): Flow<List<RoutineRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(routineRecord: RoutineRecord): Long

    @Query("DELETE FROM routine_records WHERE routineId = :routineId AND date = :date")
    suspend fun deleteRecord(routineId: Long, date: LocalDate)

}