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

    // ───────── 루틴: 조회 ─────────

    @Query("SELECT * FROM routines")
    fun observeAllRoutines(): Flow<List<Routine>> // 루틴 추가 시 자동 갱신

    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun observeRoutine(routineId: Long): Flow<Routine?>  // 삭제되면 null

    @Query("SELECT * FROM routines WHERE id = :routineId")
    suspend fun getRoutine(routineId: Long): Routine?

    // 같은 루틴으로 묶인 모든 줄 (요일 변경으로 나뉜 것 포함), 시작일 순
    @Query("SELECT * FROM routines WHERE groupId = :groupId ORDER BY startDate")
    fun observeRoutinesByGroup(groupId: String): Flow<List<Routine>>

    // ───────── 루틴: 추가 / 수정 / 삭제 ─────────

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoutine(routine: Routine): Long

    @Update
    suspend fun updateRoutine(routine: Routine)

    // 그룹 전체의 이름·카테고리·목표 수정
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

    // 반복 요일 변경: 기존 줄을 끝내고 새 줄을 시작 (둘 다 성공해야 함)
    @Query("UPDATE routines SET endDate = :endDate WHERE id = :routineId")
    suspend fun endRoutine(routineId: Long, endDate: LocalDate)

    @Transaction
    suspend fun splitRoutine(oldRoutineId: Long, endDate: LocalDate, newRoutine: Routine) {
        endRoutine(oldRoutineId, endDate)
        insertRoutine(newRoutine)
    }

    @Query("DELETE FROM routines WHERE id = :routineId")
    suspend fun deleteRoutine(routineId: Long)

    // 그룹 전체 삭제 (기록은 ForeignKey.CASCADE로 함께 삭제)
    @Query("DELETE FROM routines WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: String)

    // ───────── 기록: 조회 ─────────

    @Query("SELECT * FROM routine_records")
    fun observeAllRoutineRecords(): Flow<List<RoutineRecord>>

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

    // 그룹에 속한 모든 줄의 기록 (routine_records에는 groupId가 없어 조인)
    @Query(
        "SELECT r.* FROM routine_records r " +
                "INNER JOIN routines ON routines.id = r.routineId " +
                "WHERE routines.groupId = :groupId"
    )
    fun observeRecordsByGroup(groupId: String): Flow<List<RoutineRecord>>

    @Query("SELECT EXISTS(SELECT 1 FROM routine_records WHERE routineId = :routineId AND date = :date)")
    suspend fun hasRecord(routineId: Long, date: LocalDate): Boolean

    // ───────── 기록: 추가 / 삭제 ─────────

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(routineRecord: RoutineRecord): Long

    @Query("DELETE FROM routine_records WHERE routineId = :routineId AND date = :date")
    suspend fun deleteRecord(routineId: Long, date: LocalDate)

}
