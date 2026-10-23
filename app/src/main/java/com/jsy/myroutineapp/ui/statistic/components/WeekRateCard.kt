package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.PrimaryTrack
import com.jsy.myroutineapp.ui.theme.Typography

private val StatCardMiddleHeight = 16.dp

@Composable
fun WeekRateCard(
    total: Int,
    done: Int,
    /** 지난주 대비 달성률 변화(%p). 양수면 +, 음수면 - 로 표시 */
    lastWeek: Int,
    modifier: Modifier = Modifier
) {
    val percent = if (total == 0) 0 else (done * 100) / total
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
        Text(
            text = stringResource(R.string.Statistic_week_rate_label),
            color = OnSurfaceVariant,
            style = Typography.labelMedium,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(
                    R.string.Statistic_week_rate_value,
                    percent
                ),
                color = OnSurface,
                style = Typography.displaySmall,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(18.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_trending_up),
                    tint = Primary,
                    contentDescription = null,
                )
                Text(
                    stringResource(
                        R.string.Statistic_week_compare,
                        percent - lastWeek
                    ),
                    color = OnSurfaceVariant,
                    style = Typography.labelSmall
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(StatCardMiddleHeight),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                progress = { if (done == 0) 0f else done / total.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = Primary,
                trackColor = PrimaryTrack,
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {}  // 끝점
            )
        }
        Text(
            text = stringResource(
                R.string.Statistic_week_rate_desc,
                done,
                total
            ),
            color = OnSurfaceVariant,
            style = Typography.labelSmall,
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }

}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun WeekRateCardPreview() {
    WeekRateCard(
        total = 10,
        done = 3,
        lastWeek = 5
    )
}
