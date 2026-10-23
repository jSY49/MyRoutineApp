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
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.Typography

/** 최근 7일 막대 하나에 표시할 데이터 */
data class DayRate(
    val label: String,
    val percent: Int,
    val isToday: Boolean = false
)

@Composable
fun Recent7DayCard(
    days: List<DayRate>,
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
                text = stringResource(R.string.Statistic_week_title),
                color = OnSurface,
                style = Typography.titleSmall,
                modifier = Modifier.padding(vertical = 2.dp)
            )

        }

        // 스크롤 없이 폭에 맞춰 7칸이 균등하게 줄어들도록 Row + weight 사용
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            days.forEach { day ->
                VerticalRateBar(
                    percent = day.percent,
                    label = day.label,
                    isToday = day.isToday,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
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
private fun Recent7DayCardPreview() {
    Recent7DayCard(
        days = listOf(
            DayRate("수", 33),
            DayRate("목", 75),
            DayRate("금", 100),
            DayRate("토", 100),
            DayRate("일", 100),
            DayRate("월", 100),
            DayRate("오늘", 75, isToday = true)
        )
    )
}
