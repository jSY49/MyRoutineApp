package com.jsy.myroutineapp.ui.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.statistic.components.DayRate
import com.jsy.myroutineapp.ui.statistic.components.Recent30DaysRateCard
import com.jsy.myroutineapp.ui.statistic.components.Recent7DayCard
import com.jsy.myroutineapp.ui.statistic.components.RoutineRate
import com.jsy.myroutineapp.ui.statistic.components.StatisticHeader
import com.jsy.myroutineapp.ui.statistic.components.WeekRateCard
import com.jsy.myroutineapp.ui.theme.Background
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun StatisticScreen(modifier: Modifier = Modifier) {

    val viewModel: StatisticsViewModel = hiltViewModel()
    val (done, total) = viewModel.thisWeek.collectAsState().value
    val (lastDone, lastTotal) = viewModel.lastWeek.collectAsState().value
    val lastWeekCompletePercent = if (lastTotal == 0) 0 else lastDone * 100 / lastTotal
    val sevenDayRate = viewModel.recent7DaysRate.collectAsState().value
    val thirtyDayRate = viewModel.routines30Rates.collectAsState().value

    StatisticContent(
        weekTotalCount = total,
        weekDoneCount = done,
        lastWeekCompletePercent = lastWeekCompletePercent,
        sevenDayRate = sevenDayRate,
        thirtyDayRate = thirtyDayRate
    )
}

@Composable
private fun StatisticContent(
    weekTotalCount: Int,
    weekDoneCount: Int,
    lastWeekCompletePercent: Int,
    sevenDayRate: List<DayRate>,
    thirtyDayRate : List<RoutineRate>,
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
            StatisticHeader()
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                WeekRateCard(
                    total = weekTotalCount,
                    done = weekDoneCount,
                    lastWeek = lastWeekCompletePercent,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(end = 8.dp)
                )

            }
        }

        item {
            Recent7DayCard(
                days = sevenDayRate,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Recent30DaysRateCard(
                rates = thirtyDayRate,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Preview(
    name = "Phone",
    device = "spec:width=360dp,height=800dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Phone_flip",
    device = "spec:width=360dp,height=880dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun StatisticScreenPreview() {
    StatisticContent(
        weekTotalCount = 10,
        weekDoneCount = 3,
        lastWeekCompletePercent = 10,
        sevenDayRate = listOf(
            DayRate(
                "수",
                33
            ),
            DayRate(
                "목",
                75
            ),
            DayRate(
                "금",
                100
            ),
            DayRate(
                "토",
                100
            ),
            DayRate(
                "일",
                100
            ),
            DayRate(
                "월",
                100
            ),
            DayRate(
                "오늘",
                75,
                isToday = true
            )
        ),
        thirtyDayRate =  listOf(
            RoutineRate(
                Routine(
                    id = 1,
                    title = "물 2L 마시기",
                    category = RoutineCategory.HEALTH,
                    repeatDays = DayOfWeek.entries.toSet(),
                    description = "",
                    startDate = LocalDate.of(2026, 10, 1)
                ),
                97
            ),
            RoutineRate(
                Routine(
                    id = 2,
                    title = "운동 30분",
                    category = RoutineCategory.EXERCISE,
                    repeatDays = DayOfWeek.entries.toSet(),
                    description = "",
                    startDate = LocalDate.of(2026, 10, 1)
                ),
                77
            ),
            RoutineRate(
                Routine(
                    id = 3,
                    title = "영어 공부 20분",
                    category = RoutineCategory.STUDY,
                    repeatDays = DayOfWeek.entries.toSet(),
                    description = "",
                    startDate = LocalDate.of(2026, 10, 1)
                ),
                87
            ),
            RoutineRate(
                Routine(
                    id = 4,
                    title = "책 읽기 30분",
                    category = RoutineCategory.READING,
                    repeatDays = DayOfWeek.entries.toSet(),
                    description = "",
                    startDate = LocalDate.of(2026, 10, 1)
                ),
                70
            )
        )
    )
}