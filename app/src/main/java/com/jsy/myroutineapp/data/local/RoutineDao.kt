package com.jsy.myroutineapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.data.model.RoutineRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RoutineDao {

    //루틴
    @Query("SELECT * FROM routines")
    fun observeAllRoutines() : Flow<List<Routine>> // 루틴 추가 시 자동 갱신

    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun observeRoutine(routineId: Long): Flow<Routine?>  // 삭제되면 null

    @Query("SELECT * FROM routines WHERE groupId = :groupId ORDER BY startDate")
    fun observeRoutinesByGroup(groupId: String): Flow<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoutine(routine: Routine) : Long

    @Query("DELETE FROM routines WHERE id = :routineId")
    suspend fun deleteRoutine(routineId: Long)

    @Update
    suspend fun updateRoutine(routine : Routine)

    @Query(
        "UPDATE routines SET title = :title, category = :category, description = :description " +
                "WHERE groupId = :groupId"
    )
    suspend fun updateGroupInfo(
        groupId: String,
        title: String,
        category: RoutineCategory,
        description: String
    )

    @Query("UPDATE routines SET endDate = :endDate WHERE id = :routineId")
    suspend fun endRoutine(routineId: Long, endDate: LocalDate)

    @Transaction
    suspend fun splitRoutine(oldRoutineId: Long, endDate: LocalDate, newRoutine: Routine) {
        endRoutine(oldRoutineId, endDate)
        insertRoutine(newRoutine)
    }

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

    @Query(
        "SELECT r.* FROM routine_records r " +
                "INNER JOIN routines ON routines.id = r.routineId " +
                "WHERE routines.groupId = :groupId"
    )
    fun observeRecordsByGroup(groupId: String): Flow<List<RoutineRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(routineRecord: RoutineRecord): Long
    @Query("DELETE FROM routine_records WHERE routineId = :routineId AND date = :date")
    suspend fun deleteRecord(routineId: Long, date: LocalDate)

    @Query("DELETE FROM routines WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: String)

}