package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.IcBox
import com.jsy.myroutineapp.ui.common.iconRes
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.PrimaryTrack
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * 루틴별 달성률 한 줄: [카테고리 아이콘] 제목 ........ 97%
 *                                    ━━━━━━━━━━━━ (카테고리 색 막대)
 */
@Composable
fun HorizontalRateBar(
    percent: Int,
    routine: Routine,
    modifier: Modifier = Modifier
) {
    val color = RoutineTheme.colors.categoryColor(routine.category)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IcBox(color, routine.category.iconRes, routine.title)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = routine.title,
                    color = OnSurface,
                    style = Typography.labelLarge,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(R.string.Statistic_rate_percent, percent),
                    color = OnSurface,
                    style = Typography.labelLarge
                )
            }
            LinearProgressIndicator(
                progress = { percent.coerceIn(0, 100) / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = color.main,
                trackColor = PrimaryTrack,
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {}  // 끝점
            )
        }
    }
}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun HorizontalRateBarPreview() {
    HorizontalRateBar(
        percent = 77,
        routine = Routine(
            id = 1,
            title = "물 2L 마시기",
            category = RoutineCategory.HEALTH,
            repeatDays = DayOfWeek.entries.toSet(),
            description = "",
            startDate = LocalDate.of(
                2026,
                10,
                1
            )
        )
    )
}
