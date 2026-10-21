package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.PrimaryTrack

@Composable
fun WeekRateCard(
    total: Int,
    done: Int,
    modifier: Modifier = Modifier
) {
    val percent = if (total == 0) 0 else (done * 100) / total

    StatCard(
        label = stringResource(R.string.Statistic_week_rate_label),
        desc = stringResource(R.string.Statistic_week_rate_desc, done, total),
        modifier = modifier,
        value = {
            StatCardValue(stringResource(R.string.Statistic_week_rate_value, percent))
        },
        middle = {
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
    )
}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun WeekRateCardPreview() {
    WeekRateCard(
        total = 10,
        done = 3
    )
}
