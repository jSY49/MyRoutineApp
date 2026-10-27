package com.jsy.myroutineapp.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.AppPreferences
import com.jsy.myroutineapp.data.local.PrefKeys
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val dao: RoutineDao,
    private val appPreferences: AppPreferences
) : ViewModel() {

    // 알림 권한은 앱 설치 후 한 번만 요청
    var notificationPermissionAsked: Boolean
        get() = appPreferences.getBoolean(PrefKeys.NOTIFICATION_PERMISSION_ASKED)
        set(value) {
            appPreferences.putBoolean(
                PrefKeys.NOTIFICATION_PERMISSION_ASKED,
                value
            )
        }

    private val today = MutableStateFlow(LocalDate.now())

    val routines: StateFlow<List<Routine>> =
        combine(dao.observeAllRoutines() , today){ all, date ->
            all.filter { it.isScheduledOn(date) }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

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

    fun Routine.isScheduledOn(date: LocalDate) =
        !date.isBefore(startDate) && date.dayOfWeek in repeatDays
}