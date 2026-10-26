package com.jsy.myroutineapp.ui.statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord
import com.jsy.myroutineapp.ui.statistic.components.DayRate
import com.jsy.myroutineapp.ui.statistic.components.RoutineRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.count
import kotlin.sequences.takeWhile

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val dao: RoutineDao) : ViewModel() {

    //Todo Today 로직 수정 ( 자정 ) -> record도 해야함.
    private val today: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    private val weekStartDay: LocalDate = today.value.with(DayOfWeek.MONDAY)   //월요일
    private val routines: Flow<List<Routine>> = dao.observeAllRoutines()

    // 이번주 달성률 (월~일)
    private fun weekProgress(weekStart: LocalDate): Flow<Pair<Int, Int>> {

        val weekEnd = weekStart.plusDays(6)
        return combine(
            routines,
            dao.observeRangeRoutineRecords(
                weekStart,
                weekEnd
            )
        ) { routines, records ->

            //1. 루틴 항목 별 반복해야 하는 횟수 계산
            val scheduleCnt = totalScheduledCount(
                listRoutine = routines,
                from = weekStart,
                to = weekEnd
            )
            //2. 이번주에 완료한 루틴 횟수 계산
            records.size to scheduleCnt
        }
    }

    private fun totalScheduledCount(
        listRoutine: List<Routine>,
        from: LocalDate,
        to: LocalDate
    ): Int {

        return generateSequence(from) { it.plusDays(1) }.takeWhile { it <= to } //from 부터 to 까지 1씩 크기를 증가해 가면서
            .sumOf { date ->    // 합
                listRoutine.count { it.startDate <= date && date.dayOfWeek in it.repeatDays }   // 총 리스트의 수 ( 단, 그 루틴의 시작일 이후 + 그날짜가 반복 횟수 )
            }
        // 그니까 일정 기간동안 의 날짜를 하루하루 확인 하는데,
        //이 날짜가 시작일 이후이고, 반복 요일로 설정해둔 날 인 경우를 카운트 해서 모든 루틴의 횟수를 반환
    }

    val thisWeek = weekProgress(weekStartDay)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0 to 0
        )

    val lastWeek = weekProgress(weekStartDay.minusWeeks(1))
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0 to 0
        )

    // 최근 7일 달성률

    private fun sevenDaysProgresses(): Flow<List<DayRate>> {

        val end = today.value
        val start = end.minusDays(6)

        return combine(
            routines,
            dao.observeRangeRoutineRecords(
                start,
                end
            )
        ) { routines, records ->

            generateSequence(start) { it.plusDays(1) }.takeWhile { it <= end }
                .map { date ->

                    val scheduled =
                        routines.count { it.startDate <= date && date.dayOfWeek in it.repeatDays }   // 그날 해야 할 루틴 수
                    val done = records.count { it.date == date }        // 그날 완료한 기록 수
                    val percent = if (scheduled == 0) 0 else done * 100 / scheduled    // 달성률

                    DayRate(
                        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                        percent,
                        end == date
                    )
                }
                .toList()
        }

    }

    val recent7DaysRate = sevenDaysProgresses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    // 루틴별 달성률


    // 루틴별 연속 달성
    private fun routinesRate(start: LocalDate): Flow<List<RoutineRate>> {

        val end = start.plusDays(29)    //오늘 포함 30일 기록

        return combine(
            routines,
            dao.observeRangeRoutineRecords(
                start,
                end
            )
        ) { routines, records ->

            val doneByRoutine = records.groupBy { it.routineId }

            routines.map { routine ->

                val scheduled = scheduledCount( routine, start , end)
                val done = doneByRoutine[routine.id]?.size ?: 0

                val percent = if(scheduled == 0 ) 0 else done * 100 /scheduled
                RoutineRate(routine,percent)
            }
        }


    }

    private fun scheduledCount(
        routine: Routine,
        from: LocalDate,
        to: LocalDate
    ): Int {

        return generateSequence(from) {it.plusDays(1)}
            .takeWhile { it <= to }
            .count{ date -> routine.startDate <= date && date.dayOfWeek in routine.repeatDays}

    }

    val routines30Rates = routinesRate(today.value.minusDays(29))
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

}
