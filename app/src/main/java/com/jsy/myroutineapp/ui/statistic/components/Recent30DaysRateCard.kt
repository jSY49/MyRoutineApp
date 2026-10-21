package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate

/** 루틴별 최근 30일 달성률 한 줄에 표시할 데이터 */
data class RoutineRate(
    val routine: Routine,
    val percent: Int
)

@Composable
fun Recent30DaysRateCard(
    rates: List<RoutineRate>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .background(
                color = OnPrimary,
                shape = shape
            )
            .border(
                border = BorderStroke(
                    1.dp,
                    OutlineVariant
                ),
                shape = shape
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.Statistic_rate_title),
                color = OnSurface,
                style = Typography.titleSmall,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = stringResource(R.string.Statistic_rate_desc),
                color = OnSurfaceVariant,
                style = Typography.labelMedium,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            rates.forEach { rate ->
                HorizontalRateBar(
                    percent = rate.percent,
                    routine = rate.routine
                )
            }
        }
    }
}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun Recent30DaysRateCardPreview() {
    val start = LocalDate.of(2026, 10, 1)
    fun routine(id: Long, title: String, category: RoutineCategory) = Routine(
        id = id,
        title = title,
        category = category,
        repeatDays = DayOfWeek.entries.toSet(),
        description = "",
        startDate = start
    )

    Recent30DaysRateCard(
        rates = listOf(
            RoutineRate(routine(1, "물 2L 마시기", RoutineCategory.HEALTH), 97),
            RoutineRate(routine(2, "운동 30분", RoutineCategory.EXERCISE), 77),
            RoutineRate(routine(3, "영어 공부 20분", RoutineCategory.STUDY), 87),
            RoutineRate(routine(4, "책 읽기 30분", RoutineCategory.READING), 70)
        )
    )
}