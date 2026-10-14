package com.jsy.myroutineapp.ui.addRoutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.data.model.RoutineRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class AddRoutineViewModel(private val dao: RoutineDao) : ViewModel() {

    fun save(
        title: String,
        category: RoutineCategory,
        repeatDays: Set<DayOfWeek>,
        description: String,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            dao.insertRoutine(
                Routine(
                    title = title.trim(),
                    category = category,
                    repeatDays = repeatDays,
                    description = description.trim(),
                    startDate = LocalDate.now()
                )
            )
            onSaved()
        }
    }
}