package com.jsy.myroutineapp.ui.addRoutine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsy.myroutineapp.data.local.RoutineDao
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddRoutineViewModel @Inject constructor(
    private val dao: RoutineDao,
    savedStateHandle: SavedStateHandle   // 수정 화면일 때만 routineId가 들어옴
) : ViewModel() {

    // null이면 새 루틴 추가, 값이 있으면 그 루틴 수정
    private val editingId: Long? = savedStateHandle[Routes.ROUTINE_ID_ARG]

    val isEdit: Boolean = editingId != null

    // 수정할 루틴 (입력창 초기값, 요일 변경 여부 비교에 사용)
    private val _editing = MutableStateFlow<Routine?>(null)
    val editing: StateFlow<Routine?> = _editing

    init {
        editingId?.let { id ->
            viewModelScope.launch { _editing.value = dao.getRoutine(id) }
        }
    }

    fun save(
        title: String,
        category: RoutineCategory,
        repeatDays: Set<DayOfWeek>,
        description: String,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            val id = editingId
            if (id == null) {
                dao.insertRoutine(
                    Routine(
                        title = title.trim(),
                        category = category,
                        repeatDays = repeatDays,
                        description = description.trim(),
                        startDate = LocalDate.now()
                    )
                )
            } else {
                update(id, title.trim(), category, repeatDays, description.trim())
            }
            onSaved()
        }
    }

    private suspend fun update(
        id: Long,
        title: String,
        category: RoutineCategory,
        repeatDays: Set<DayOfWeek>,
        description: String
    ) {
        val current = dao.getRoutine(id) ?: return

        if (repeatDays != current.repeatDays) {
            val today = LocalDate.now()
            // 오늘 이미 완료 기록이 있으면 오늘 칸이 바뀌지 않도록 내일부터 새 요일 적용
            val effective = if (dao.hasRecord(current.id, today)) today.plusDays(1) else today

            if (current.startDate >= effective) {
                // 아직 시작 전이거나 오늘 만든 루틴: 과거가 없으니 나누지 않고 그대로 덮어씀
                dao.updateRoutine(current.copy(repeatDays = repeatDays))
            } else {
                // 기존 줄은 어제(또는 오늘)까지로 끝내고, 같은 groupId로 새 줄을 시작
                dao.splitRoutine(
                    oldRoutineId = current.id,
                    endDate = effective.minusDays(1),
                    newRoutine = current.copy(
                        id = 0,
                        repeatDays = repeatDays,
                        startDate = effective,
                        endDate = null
                    )
                )
            }
        }

        // 이름·카테고리·목표는 같은 그룹 전체에 반영
        dao.updateGroupInfo(current.groupId, title, category, description)
    }
}
