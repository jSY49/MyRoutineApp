package com.jsy.myroutineapp.ui.addRoutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddRoutineViewModel @Inject constructor(private val dao: RoutineDao) : ViewModel() {

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