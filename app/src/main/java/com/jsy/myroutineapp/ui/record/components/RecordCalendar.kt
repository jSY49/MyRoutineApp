package com.jsy.myroutineapp.ui.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import com.jsy.myroutineapp.ui.theme.Error

@Composable
fun RecordCalendar(
    month: YearMonth,
    selected: LocalDate,
    today: LocalDate,
    rates: Map<LocalDate, Float>,   // 날짜별 루틴 달성률
    onPrev: () -> Unit, onNext: () -> Unit,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            MonthHeader(
                month = month,
                canGoNext = month < YearMonth.from(today),
                onPrev = onPrev,
                onNext = onNext
            )

            WeekdayRow(
                Modifier.padding(
                    top = 12.dp,
                    bottom = 4.dp
                )
            )

            CalendarGrid(month = month) { date, cellModifier ->
                CalendarDayCell(
                    day = date.dayOfMonth,
                    rate = rates[date] ?: 0f,
                    isSelected = date == selected,
                    isToday = date == today,
                    isFuture = date.isAfter(today),
                    onClick = { onSelect(date) },
                    modifier = cellModifier
                )
            }
            CalendarLegend(Modifier.padding(top = 16.dp))
        }
    }
}

// 해당 월의 날짜 셀을 일요일 시작 7열 격자로 배치. 셀 모양은 cell 에서 정한다.
@Composable
fun CalendarGrid(
    month: YearMonth,
    modifier: Modifier = Modifier,
    cell: @Composable (date: LocalDate, modifier: Modifier) -> Unit
) {
    val offset = month.atDay(1).dayOfWeek.value % 7
    val cells: List<LocalDate?> =
        List(offset) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }

    Column(modifier) {
        cells.chunked(7)
            .forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    week.forEach { date ->
                        if (date == null) {
                            Box(modifier = Modifier.weight(1f))
                        } else {
                            cell(date, Modifier.weight(1f))
                        }
                    }
                    repeat(7 - week.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
    }
}

@Composable
fun MonthHeader(
    month: YearMonth,
    canGoNext: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    canGoPrev: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPrev,
            enabled = canGoPrev
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_left),
                contentDescription = "이전 달",
                tint = if (canGoPrev) OnSurface else OnSurfaceVariant.copy(alpha = 0.4f)
            )
        }
        Text(
            text = "${month.year}년 ${month.monthValue}월",
            style = Typography.titleMedium,
            color = OnSurface
        )
        IconButton(
            onClick = onNext,
            enabled = canGoNext
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = "다음 달",
                tint = if (canGoNext) OnSurface else OnSurfaceVariant.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun WeekdayRow(modifier: Modifier = Modifier) {
    val days = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )
    Row(modifier.fillMaxWidth()) {
        days.forEach { d ->
            Text(
                text = d.getDisplayName(
                    TextStyle.NARROW,
                    Locale.KOREA
                ), // 일 월 화 ...
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = Typography.labelMedium,
                color = if (d == DayOfWeek.SUNDAY) Error else OnSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordCalendarPreview() {
    val today = LocalDate.of(
        2026,
        10,
        6
    )
    RecordCalendar(
        month = YearMonth.of(
            2026,
            10
        ),
        selected = today,
        today = today,
        rates = mapOf(
            LocalDate.of(
                2026,
                10,
                1
            ) to 0.6f,
            LocalDate.of(
                2026,
                10,
                2
            ) to 1f,
            LocalDate.of(
                2026,
                10,
                3
            ) to 1f,
            LocalDate.of(
                2026,
                10,
                4
            ) to 1f,
            LocalDate.of(
                2026,
                10,
                5
            ) to 1f,
            LocalDate.of(
                2026,
                10,
                6
            ) to 0.7f,
        ),
        onPrev = {},
        onNext = {},
        onSelect = {}
    )
}
