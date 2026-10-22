package com.jsy.myroutineapp.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val dao: RoutineDao) : ViewModel() {

    val routines: StateFlow<List<Routine>> = dao.observeAllRoutines()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val today = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val records: StateFlow<List<RoutineRecord>> = today
        .flatMapLatest { date -> dao.observeRangeRoutineRecords(date, date) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // 화면이 다시 보일 때 호출해서 오늘 날짜로 갱신
    fun refreshToday() {
        today.value = LocalDate.now()
    }

    fun setChecked(routineId: Long, checked: Boolean) {
        val date = today.value
        viewModelScope.launch {
            if (checked) dao.insertRecord(RoutineRecord(routineId = routineId, date = date))
            else dao.deleteRecord(routineId, date)
        }
    }
}