package com.jsy.myroutineapp.ui.statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val dao: RoutineDao) : ViewModel() {

    //Todo Today 로직 수정 ( 자정 ) -> record도 해야함.
    private val routines = dao.observeAllRoutines()
    private val today = MutableStateFlow(LocalDate.now())
    private val weekStartDay = today.value.with(DayOfWeek.MONDAY)
    private val weekEndDay = weekStartDay.plusDays(6)

    private val weekRecords = dao.observeRangeRoutineRecords(weekStartDay, weekEndDay)

    // 이번주 달성률
    private fun scheduledCount(routines: List<Routine>, from: LocalDate, to: LocalDate): Int =
        // generateSequence(시작값) { 다음값 }: from부터 하루씩 더한 날짜를 무한히 만드는 시퀀스 (it = 직전 날짜)
        // takeWhile { 조건 }: 조건이 참인 동안만 취함 -> to를 넘으면 멈춤 (LocalDate는 <= 비교 가능)
        // sumOf { }: 날짜마다 블록이 돌려주는 숫자를 모두 더함
        generateSequence(from) { it.plusDays(1) }.takeWhile { it <= to }.sumOf { date ->
                // count { 조건 }: 조건이 참인 루틴 개수 (it = 루틴 하나)
                // 그날 이미 시작했고(startDate <= date) 반복 요일에 포함(in = Set.contains)되는 루틴만 셈
                routines.count { it.startDate <= date && date.dayOfWeek in it.repeatDays }
            }

    // Pair(done, total). StateFlow = 항상 현재 값을 가지는 Flow
    val weekProgress: StateFlow<Pair<Int, Int>> = combine(
        routines, weekRecords
    ) { routines, records ->  //flow를 두개 묶어서 하나라도 새 값을 보내면 최신 값 두개로 다시 실행
        // A to B: Pair(A, B)를 만드는 중위 함수 / 앞 = 이번주 완료 횟수, 뒤 = 분모
        records.size to scheduledCount(routines, weekStartDay, today.value)
    // stateIn: Flow -> StateFlow 변환
    // viewModelScope: ViewModel이 살아있는 동안 유지
    // WhileSubscribed(5_000): 화면이 구독 중일 때만 DB를 관찰, 구독이 끊기면 5초 뒤 중단 (화면 회전 등은 버팀)
    // 0 to 0: 첫 계산 전에 보여줄 초기값
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0 to 0)

    //최장 연속 루틴
    // 루틴별 연속 달성
    // 최근 7일 달성률
    // 루틴별 달성률

}