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
import com.jsy.myroutineapp.ui.statistic.components.BestStreakCard
import com.jsy.myroutineapp.ui.statistic.components.DayRate
import com.jsy.myroutineapp.ui.statistic.components.Recent30DaysRateCard
import com.jsy.myroutineapp.ui.statistic.components.Recent7DayCard
import com.jsy.myroutineapp.ui.statistic.components.RoutineRate
import com.jsy.myroutineapp.ui.statistic.components.RoutineStreak
import com.jsy.myroutineapp.ui.statistic.components.RoutineStreakCard
import com.jsy.myroutineapp.ui.statistic.components.StatisticHeader
import com.jsy.myroutineapp.ui.statistic.components.WeekRateCard
import com.jsy.myroutineapp.ui.theme.Background
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun StatisticScreen(modifier: Modifier = Modifier) {

    val viewModel: StatisticsViewModel = hiltViewModel()
    val (done, total) = viewModel.weekProgress.collectAsState().value

    StatisticContent(
        weekTotalCount = total,
        weekDoneCount = done,
    )
}

@Composable
private fun StatisticContent(
    weekTotalCount : Int, 
    weekDoneCount : Int,
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
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                BestStreakCard(
                    day = 10,
                    name = "물마시기",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        }

        item {
            RoutineStreakCard(
                streaks = listOf(
                    RoutineStreak(
                        sampleRoutine(1, "물 2L 마시기", RoutineCategory.HEALTH), 23, 23,
                        List(7) { true }
                    ),
                    RoutineStreak(
                        sampleRoutine(2, "운동 30분", RoutineCategory.EXERCISE), 3, 6,
                        listOf(true, true, true, false, true, true, true)
                    ),
                    RoutineStreak(
                        sampleRoutine(3, "영어 공부 20분", RoutineCategory.STUDY), 6, 16,
                        listOf(false, true, true, true, true, true, true)
                    ),
                    RoutineStreak(
                        sampleRoutine(4, "책 읽기 30분", RoutineCategory.READING), 5, 9,
                        listOf(false, false, true, true, true, true, true)
                    )
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Recent7DayCard(
                days = listOf(
                    DayRate("수", 33),
                    DayRate("목", 75),
                    DayRate("금", 100),
                    DayRate("토", 100),
                    DayRate("일", 100),
                    DayRate("월", 100),
                    DayRate("오늘", 75, isToday = true)
                ),
                weekDiff = 5,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Recent30DaysRateCard(
                rates = listOf(
                    RoutineRate(sampleRoutine(1, "물 2L 마시기", RoutineCategory.HEALTH), 97),
                    RoutineRate(sampleRoutine(2, "운동 30분", RoutineCategory.EXERCISE), 77),
                    RoutineRate(sampleRoutine(3, "영어 공부 20분", RoutineCategory.STUDY), 87),
                    RoutineRate(sampleRoutine(4, "책 읽기 30분", RoutineCategory.READING), 70)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

// 목업 데이터용. ViewModel 연결 후 제거
private fun sampleRoutine(id: Long, title: String, category: RoutineCategory) = Routine(
    id = id,
    title = title,
    category = category,
    repeatDays = DayOfWeek.entries.toSet(),
    description = "",
    startDate = LocalDate.of(2026, 10, 1)
)

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
    )
}