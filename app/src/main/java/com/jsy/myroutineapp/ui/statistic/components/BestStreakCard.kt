package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.Streak

@Composable
fun BestStreakCard(
    day: Int,
    name: String,
    modifier: Modifier = Modifier
) {
    StatCard(
        label = stringResource(R.string.Statistic_best_streak_label),
        desc = name,
        modifier = modifier,
        value = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_local_fire_department_fill),
                    contentDescription = null,
                    tint = Streak,
                    modifier = Modifier.size(28.dp)
                )
                StatCardValue(stringResource(R.string.Statistic_best_streak_value, day))
            }
        }
    )
}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun BestStreakCardPreview() {
    BestStreakCard(
        day = 24,
        name = "물 마시기"
    )
}
