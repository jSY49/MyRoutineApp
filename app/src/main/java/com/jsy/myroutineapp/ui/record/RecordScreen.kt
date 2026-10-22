package com.jsy.myroutineapp.ui.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.MultipleDevicePreview
import com.jsy.myroutineapp.ui.common.toRepeatText
import com.jsy.myroutineapp.ui.record.components.RecordCalendar
import com.jsy.myroutineapp.ui.record.components.RecordHeader
import com.jsy.myroutineapp.ui.record.components.RecordRoutineItem
import com.jsy.myroutineapp.ui.theme.Background
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.PrimaryText
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.material3.Surface as M3Surface

// ViewModel/DB를 다루는 화면 (프리뷰 불가)
@Composable
fun RecordScreen(modifier: Modifier = Modifier) {

    val viewModel: RecordViewModel = hiltViewModel()

    val month by viewModel.month.collectAsStateWithLifecycle()
    val selected by viewModel.selected.collectAsStateWithLifecycle()
    val rates by viewModel.rates.collectAsStateWithLifecycle()
    val dayItems by viewModel.dayItems.collectAsStateWithLifecycle()

    RecordContent(
        month = month,
        selected = selected,
        today = viewModel.today,
        rates = rates,
        dayItems = dayItems,
        onTodayClick = viewModel::goToday,
        onPrevMonth = viewModel::prevMonth,
        onNextMonth = viewModel::nextMonth,
        onSelectDate = viewModel::selectDate,
        onToggle = viewModel::setChecked,
        modifier = modifier
    )
}

@Composable
private fun RecordContent(
    month: YearMonth,
    selected: LocalDate,
    today: LocalDate,
    rates: Map<LocalDate, Float>,
    dayItems: List<DayRoutineItem>,
    onTodayClick: () -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onToggle: (routineId: Long, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 24.dp
        )
    ) {
        item {
            RecordHeader(onTodayClick = onTodayClick)
        }

        item {
            RecordCalendar(
                month = month,
                selected = selected,
                today = today,
                rates = rates,
                onPrev = onPrevMonth,
                onNext = onNextMonth,
                onSelect = onSelectDate
            )
        }

        item {
            DaySummary(
                selected = selected,
                isToday = selected == today,
                done = dayItems.count { it.done },
                total = dayItems.size,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (dayItems.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.Record_empty),
                    style = Typography.bodyMedium,
                    color = OnSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )
            }
        } else {
            items(
                items = dayItems,
                key = { it.routine.id }
            ) { item ->
                RecordRoutineItem(
                    category = item.routine.category,
                    title = item.routine.title,
                    repeatText = item.routine.repeatDays.toRepeatText(),
                    isDone = item.done,
                    onToggle = { onToggle(item.routine.id, !item.done) }
                )
            }

            item {
                Text(
                    text = stringResource(R.string.Record_edit_hint),
                    style = Typography.bodySmall,
                    color = RoutineTheme.colors.textTertiary,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun DaySummary(
    selected: LocalDate,
    isToday: Boolean,
    done: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val weekday = selected.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    val title = stringResource(
        if (isToday) R.string.Record_date_title_today else R.string.Record_date_title,
        selected.monthValue,
        selected.dayOfMonth,
        weekday
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Typography.titleMedium,
            color = OnSurface
        )

        if (total > 0) {
            M3Surface(
                shape = CircleShape,
                color = RoutineTheme.colors.primarySoft
            ) {
                Text(
                    text = stringResource(R.string.Record_complete_count, done, total),
                    style = Typography.labelMedium,
                    color = PrimaryText,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@MultipleDevicePreview
@Composable
private fun RecordContentPreview() {
    val today = LocalDate.of(2026, 10, 6)
    val routines = listOf(
        Routine(1, "물 2L 마시기", RoutineCategory.HEALTH, DayOfWeek.entries.toSet(), "", today),
        Routine(2, "운동 30분", RoutineCategory.EXERCISE, setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "", today),
        Routine(3, "영어 공부 20분", RoutineCategory.STUDY, DayOfWeek.entries.toSet(), "", today),
    )

    RecordContent(
        month = YearMonth.of(2026, 10),
        selected = today,
        today = today,
        rates = mapOf(
            LocalDate.of(2026, 10, 1) to 0.6f,
            LocalDate.of(2026, 10, 2) to 1f,
            LocalDate.of(2026, 10, 3) to 1f,
            LocalDate.of(2026, 10, 6) to 0.7f,
        ),
        dayItems = routines.mapIndexed { i, r -> DayRoutineItem(r, i != 1) },
        onTodayClick = {},
        onPrevMonth = {},
        onNextMonth = {},
        onSelectDate = {},
        onToggle = { _, _ -> }
    )
}
