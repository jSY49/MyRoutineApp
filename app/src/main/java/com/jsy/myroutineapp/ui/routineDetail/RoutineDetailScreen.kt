package com.jsy.myroutineapp.ui.routineDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.data.model.isScheduledOn
import com.jsy.myroutineapp.ui.common.MultipleDevicePreview
import com.jsy.myroutineapp.ui.common.iconRes
import com.jsy.myroutineapp.ui.common.labelRes
import com.jsy.myroutineapp.ui.record.components.CalendarDayCell
import com.jsy.myroutineapp.ui.record.components.CalendarGrid
import com.jsy.myroutineapp.ui.record.components.MonthHeader
import com.jsy.myroutineapp.ui.record.components.WeekdayRow
import com.jsy.myroutineapp.ui.theme.Background
import com.jsy.myroutineapp.ui.theme.CategoryColor
import com.jsy.myroutineapp.ui.theme.ErrorContainer
import com.jsy.myroutineapp.ui.theme.ErrorOutline
import com.jsy.myroutineapp.ui.theme.ErrorText
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Outline
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.SurfaceContainer
import com.jsy.myroutineapp.ui.theme.TextTertiary
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// 캘린더 한 칸의 상태
private enum class DayState {
    DONE,       // 완료
    MISSED,     // 예정이었지만 하지 못함
    PENDING,    // 오늘 예정, 아직 안 함
    UPCOMING,   // 앞으로 예정
    OFF         // 예정 없음
}

// 최근 기록 한 줄의 상태
private enum class RecordStatus { DONE, MISSED, PENDING }

private data class MonthRecord(val date: LocalDate, val status: RecordStatus)

@Composable
fun RoutineDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: RoutineDetailViewModel = hiltViewModel()
    val routine by viewModel.routine.collectAsStateWithLifecycle()
    val doneDates by viewModel.doneDates.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()

    // 불러오는 중이거나 삭제된 루틴이면 그리지 않음
    routine?.let {
        RoutineDetailContent(
            routine = it,
            doneDates = doneDates,
            month = month,
            today = viewModel.today,
            onPrevMonth = viewModel::prevMonth,
            onNextMonth = viewModel::nextMonth,
            onToggle = viewModel::setChecked,
            onBack = onBack,
            onEdit = {},    // TODO: 수정 화면 이동
            onMore = {},    // TODO: 더보기 메뉴(삭제 등)
            modifier = modifier
        )
    }
}

@Composable
private fun RoutineDetailContent(
    routine: Routine,
    doneDates: Set<LocalDate>,
    month: YearMonth,
    today: LocalDate,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToggle: (date: LocalDate, checked: Boolean) -> Unit,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = RoutineTheme.colors.categoryColor(routine.category)
    val records = remember(routine, doneDates, month, today) {
        monthRecords(routine, doneDates, month, today)
    }

    Scaffold(
        modifier = modifier,
        containerColor = Background,
        topBar = {
            DetailTopBar(
                color = color,
                onBack = onBack,
                onEdit = onEdit,
                onMore = onMore
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 24.dp
            )
        ) {
            item {
                RoutineProfile(
                    routine = routine,
                    color = color
                )
            }

            item {
                DetailCalendarCard(
                    routine = routine,
                    doneDates = doneDates,
                    month = month,
                    today = today,
                    color = color,
                    onPrevMonth = onPrevMonth,
                    onNextMonth = onNextMonth
                )
            }

            item {
                MonthRecordCard(
                    month = month,
                    records = records,
                    onToggle = onToggle
                )
            }
        }
    }
}

@Composable
private fun DetailTopBar(
    color: CategoryColor,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onMore: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()    // 직접 만든 topBar라 상태바 영역을 직접 비워줘야 함
            .padding(
                horizontal = 4.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.RoutineDetail_back),
                tint = OnSurface
            )
        }

        Spacer(Modifier.weight(1f))

        Surface(
            onClick = onEdit,
            shape = CircleShape,
            color = color.container
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 8.dp
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    tint = color.main,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(R.string.RoutineDetail_edit),
                    style = Typography.labelLarge,
                    color = color.main
                )
            }
        }

        IconButton(onClick = onMore) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = stringResource(R.string.RoutineDetail_more),
                tint = OnSurface
            )
        }
    }
}

// 아이콘 + 제목 + 설명 + 반복 요일
@Composable
private fun RoutineProfile(
    routine: Routine,
    color: CategoryColor,
    modifier: Modifier = Modifier
) {
    val subtitle = listOf(
        stringResource(routine.category.labelRes),
        routine.description
    ).filter { it.isNotBlank() }.joinToString(" · ")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(88.dp)
                .background(
                    color.container,
                    RoundedCornerShape(28.dp)
                )
        ) {
            Icon(
                painter = painterResource(routine.category.iconRes),
                contentDescription = null,
                tint = color.main,
                modifier = Modifier.size(44.dp)
            )
        }

        Text(
            text = routine.title,
            style = Typography.headlineSmall,
            color = OnSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = subtitle,
            style = Typography.bodyMedium,
            color = OnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        RepeatDayChips(
            repeatDays = routine.repeatDays,
            color = color,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

// 월~일 7개 중 반복 요일만 카테고리 색으로 채운다
@Composable
private fun RepeatDayChips(
    repeatDays: Set<DayOfWeek>,
    color: CategoryColor,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DayOfWeek.entries.forEach { day ->
            val selected = day in repeatDays
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (selected) color.main else SurfaceContainer)
            ) {
                Text(
                    text = day.getDisplayName(
                        TextStyle.NARROW,
                        Locale.KOREAN
                    ),
                    style = Typography.labelMedium,
                    color = if (selected) Color.White else TextTertiary
                )
            }
        }
    }
}

@Composable
private fun DetailCalendarCard(
    routine: Routine,
    doneDates: Set<LocalDate>,
    month: YearMonth,
    today: LocalDate,
    color: CategoryColor,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            MonthHeader(
                month = month,
                canGoPrev = month > YearMonth.from(routine.startDate),
                canGoNext = month < YearMonth.from(today),
                onPrev = onPrevMonth,
                onNext = onNextMonth
            )

            WeekdayRow(
                Modifier.padding(
                    top = 12.dp,
                    bottom = 4.dp
                )
            )

            CalendarGrid(month = month) { date, cellModifier ->
                val state = dayState(routine, doneDates, today, date)

                CalendarDayCell(
                    day = date.dayOfMonth,
                    rate = 0f,
                    isSelected = false,
                    isToday = date == today,
                    isFuture = false,
                    onClick = {},
                    modifier = cellModifier,
                    containerColor = if (state == DayState.DONE) color.main else Color.Transparent,
                    contentColor = when (state) {
                        DayState.DONE -> Color.White
                        DayState.OFF -> OnSurfaceVariant.copy(alpha = 0.4f)
                        else -> OnSurface
                    },
                    ringColor = when (state) {
                        DayState.MISSED -> ErrorOutline
                        DayState.PENDING, DayState.UPCOMING -> Outline
                        else -> null
                    },
                    accentColor = color.main,
                    showTodayDot = true,    // 오늘은 테두리 대신 점으로 표시
                    enabled = false
                )
            }

            DetailLegend(
                color = color,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun DetailLegend(
    color: CategoryColor,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            Alignment.End
        )
    ) {
        LegendDot(color = color.main)
        Text(
            text = stringResource(R.string.RoutineDetail_legend_done),
            style = Typography.labelSmall,
            color = OnSurfaceVariant
        )
        Spacer(Modifier.size(4.dp))
        LegendDot(color = ErrorOutline)
        Text(
            text = stringResource(R.string.RoutineDetail_legend_missed),
            style = Typography.labelSmall,
            color = OnSurfaceVariant
        )
    }
}

@Composable
private fun LegendDot(color: Color) {
    Box(
        Modifier
            .size(10.dp)
            .background(
                color,
                CircleShape
            )
    )
}

@Composable
private fun MonthRecordCard(
    month: YearMonth,
    records: List<MonthRecord>,
    onToggle: (date: LocalDate, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(
                    R.string.RoutineDetail_month_record_title,
                    month.monthValue
                ),
                style = Typography.titleMedium,
                color = OnSurface
            )

            if (records.isEmpty()) {
                Text(
                    text = stringResource(R.string.RoutineDetail_month_record_empty),
                    style = Typography.bodyMedium,
                    color = OnSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                Spacer(Modifier.height(4.dp))
                records.forEachIndexed { index, record ->
                    if (index > 0) HorizontalDivider(color = OutlineVariant)
                    MonthRecordRow(
                        record = record,
                        onToggle = { onToggle(record.date, record.status != RecordStatus.DONE) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthRecordRow(
    record: MonthRecord,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = record.date
    val isToday = record.status == RecordStatus.PENDING

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                R.string.RoutineDetail_record_date,
                date.monthValue,
                date.dayOfMonth,
                date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.KOREAN
                )
            ),
            style = Typography.bodyMedium,
            color = OnSurface
        )

        if (isToday) {
            Surface(
                shape = CircleShape,
                color = RoutineTheme.colors.primarySoft
            ) {
                Text(
                    text = stringResource(R.string.RoutineDetail_today_badge),
                    style = Typography.labelSmall,
                    color = RoutineTheme.colors.primaryText,
                    modifier = Modifier.padding(
                        horizontal = 6.dp,
                        vertical = 2.dp
                    )
                )
            }
        }

        Spacer(Modifier.weight(1f))

        StatusBadge(
            status = record.status,
            onClick = onToggle
        )
    }
}

@Composable
private fun StatusBadge(
    status: RecordStatus,
    onClick: () -> Unit
) {
    val extended = RoutineTheme.colors

    val container: Color
    val content: Color
    val icon: Int
    val label: Int
    when (status) {
        RecordStatus.DONE -> {
            container = extended.successContainerHigh
            content = extended.successText
            icon = R.drawable.ic_check_circle_fill
            label = R.string.RoutineDetail_status_done
        }

        RecordStatus.MISSED -> {
            container = ErrorContainer
            content = ErrorText
            icon = R.drawable.ic_cancel_fill
            label = R.string.RoutineDetail_status_missed
        }

        RecordStatus.PENDING -> {
            container = SurfaceContainer
            content = OnSurfaceVariant
            icon = R.drawable.ic_radio_button_unchecked
            label = R.string.RoutineDetail_status_pending
        }
    }

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = container
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(label),
                style = Typography.labelMedium,
                color = content
            )
        }
    }
}

private fun dayState(
    routine: Routine,
    doneDates: Set<LocalDate>,
    today: LocalDate,
    date: LocalDate
): DayState = when {
    date in doneDates -> DayState.DONE
    !routine.isScheduledOn(date) -> DayState.OFF
    date == today -> DayState.PENDING
    date.isBefore(today) -> DayState.MISSED
    else -> DayState.UPCOMING
}

// 선택한 달에서 예정이었던 날을 최신순으로 (오늘 이후와 시작일 이전은 제외)
private fun monthRecords(
    routine: Routine,
    doneDates: Set<LocalDate>,
    month: YearMonth,
    today: LocalDate
): List<MonthRecord> {
    val first = maxOf(month.atDay(1), routine.startDate)
    val last = minOf(month.atEndOfMonth(), today)

    return generateSequence(last) { it.minusDays(1) }
        .takeWhile { !it.isBefore(first) }
        .filter { routine.isScheduledOn(it) }
        .map { date ->
            MonthRecord(
                date = date,
                status = when {
                    date in doneDates -> RecordStatus.DONE
                    date == today -> RecordStatus.PENDING
                    else -> RecordStatus.MISSED
                }
            )
        }
        .toList()
}

@MultipleDevicePreview
@Composable
private fun RoutineDetailContentPreview() {
    val today = LocalDate.of(
        2026,
        10,
        6
    )
    RoutineDetailContent(
        routine = Routine(
            id = 1,
            title = "운동 30분",
            category = RoutineCategory.EXERCISE,
            repeatDays = setOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY
            ),
            description = "목표 30분",
            startDate = LocalDate.of(
                2026,
                9,
                28
            )
        ),
        doneDates = setOf(
            LocalDate.of(
                2026,
                9,
                29
            ),
            LocalDate.of(
                2026,
                10,
                1
            ),
            LocalDate.of(
                2026,
                10,
                5
            )
        ),
        month = YearMonth.from(today),
        today = today,
        onPrevMonth = {},
        onNextMonth = {},
        onToggle = { _, _ -> },
        onBack = {},
        onEdit = {},
        onMore = {}
    )
}
