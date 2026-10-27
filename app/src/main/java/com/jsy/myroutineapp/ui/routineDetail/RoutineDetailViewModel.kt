package com.jsy.myroutineapp.ui.routineDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord
import com.jsy.myroutineapp.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    private val dao: RoutineDao,
    savedStateHandle: SavedStateHandle   // 네비게이션 인자(routineId)가 자동으로 들어옴
) : ViewModel() {

    val today: LocalDate = LocalDate.now()

    private val _month = MutableStateFlow(YearMonth.from(today))
    val month: StateFlow<YearMonth> = _month

    private val routineId: Long = checkNotNull(savedStateHandle[Routes.ROUTINE_ID_ARG])

    private val routineFlow: Flow<Routine?> = dao.observeRoutine(routineId)

    // null: 아직 불러오는 중이거나 삭제된 루틴
    val routine: StateFlow<Routine?> = routineFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )

    // 루틴 시작일 ~ 오늘 사이에 완료한 날짜
    @OptIn(ExperimentalCoroutinesApi::class)
    val doneDates: StateFlow<Set<LocalDate>> = routineFlow
        .flatMapLatest { routine ->
            if (routine == null) emptyFlow()
            else dao.observeRangeSpecialRoutineRecord(
                routineId = routine.id,
                from = routine.startDate,
                to = today
            )
        }
        .map { records -> records.map { it.date }.toSet() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptySet()
        )

    // 해당 날짜의 완료 기록 추가/삭제 (완료한 것만 DB에 저장)
    fun setChecked(date: LocalDate, checked: Boolean) {
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

    // 루틴 시작 달 이전, 이번 달 이후로는 이동하지 않음
    fun prevMonth() {
        val start = routine.value?.startDate?.let { YearMonth.from(it) } ?: return
        if (_month.value > start) _month.value = _month.value.minusMonths(1)
    }

    fun nextMonth() {
        if (_month.value < YearMonth.from(today)) _month.value = _month.value.plusMonths(1)
    }
}
