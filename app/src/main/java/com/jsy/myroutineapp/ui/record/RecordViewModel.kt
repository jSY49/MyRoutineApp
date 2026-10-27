package com.jsy.myroutineapp.ui.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord
import com.jsy.myroutineapp.data.model.isScheduledOn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(private val dao: RoutineDao) : ViewModel() {

    val today: LocalDate = LocalDate.now()

    private val _month = MutableStateFlow(YearMonth.from(today))
    val month: StateFlow<YearMonth> = _month

    private val _selected = MutableStateFlow(today)
    val selected: StateFlow<LocalDate> = _selected

    private val routines = dao.observeAllRoutines()

    // 해당 월 기록 (월이 바뀌면 다시 조회)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val monthRecords = _month.flatMapLatest { m ->
        dao.observeRangeRoutineRecords(
            m.atDay(1),
            m.atEndOfMonth()
        )
    }

    // 캘린더: 날짜 -> 달성률
    val rates: StateFlow<Map<LocalDate, Float>> =
        combine(
            _month,
            routines,
            monthRecords
        ) { m, all, records ->
            val doneByDate = records.groupBy { it.date }
            (1..m.lengthOfMonth()).map { m.atDay(it) }
                .associateWith { date ->
                    val scheduled = all.filter { it.isScheduledOn(date) }
                    if (scheduled.isEmpty()) 0f
                    else {
                        val done = doneByDate[date].orEmpty()
                            .map { it.routineId }
                            .toSet()
                        scheduled.count { it.id in done } / scheduled.size.toFloat()
                    }
                }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )

    // 아래 목록: 선택한 날짜의 루틴 + 완료 여부
    @OptIn(ExperimentalCoroutinesApi::class)
    val dayItems: StateFlow<List<DayRoutineItem>> =
        combine(
            _selected,
            routines
        ) { date, all -> date to all }
            .flatMapLatest { (date, all) ->
                dao.observeRangeRoutineRecords(
                    date,
                    date
                )
                    .map { records ->
                        val done = records.map { it.routineId }
                            .toSet()
                        all.filter { it.isScheduledOn(date) }
                            .map {
                                DayRoutineItem(
                                    it,
                                    it.id in done
                                )
                            }
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun selectDate(date: LocalDate) {
        _selected.value = date
    }

    fun prevMonth() {
        _month.value = _month.value.minusMonths(1)
    }

    fun nextMonth() {
        _month.value = _month.value.plusMonths(1)
    }

    fun goToday() {
        _month.value = YearMonth.from(today); _selected.value = today
    }

    fun setChecked(routineId: Long, checked: Boolean) {
        val date = _selected.value
        viewModelScope.launch {
            if (checked) dao.insertRecord(
                RoutineRecord(
                    routineId = routineId,
                    date = date
                )
            )
            else dao.deleteRecord(
                routineId,
                date
            )
        }
    }
}

data class DayRoutineItem(val routine: Routine, val done: Boolean)

