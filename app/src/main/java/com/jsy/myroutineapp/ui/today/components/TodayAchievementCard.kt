package com.jsy.myroutineapp.ui.today.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.Surface
import com.jsy.myroutineapp.ui.theme.SurfaceContainerHigh
import com.jsy.myroutineapp.ui.theme.TextTertiary
import com.jsy.myroutineapp.ui.theme.Typography
import com.jsy.myroutineapp.ui.theme.caption

@Composable
fun TodayAchievementCard(total: Int, done: Int, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, OutlineVariant),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(88.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {

                CircularProgressIndicator(
                    progress = { if (done == 0) 0f else done / total.toFloat() },          // 0f ~ 1f
                    modifier = Modifier.fillMaxSize(),
                    color = Primary,                      // 진행 부분
                    trackColor = SurfaceContainerHigh,                 // 뒤의 연한 원
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Round,      // 시안의 둥근 끝
                    gapSize = 0.dp                    // 트랙과 진행 사이 틈 제거
                )

                val percent = if (total == 0) 0 else (done * 100) / total
                Text(
                    text = "${percent}%",
                    style = Typography.headlineSmall
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(
                    4.dp,
                    Alignment.CenterVertically
                )
            ) {
                Text(
                    text = stringResource(R.string.Today_achievement_label),
                    style = Typography.caption,
                    color = OnSurfaceVariant
                )
                Text(
                    text = stringResource(
                        R.string.Today_achievement_count,
                        done,
                        total
                    ),
                    style = Typography.titleLarge
                )
                Text(
                    text =
                        if (total > done) {
                            stringResource(
                                R.string.Today_achievement_msg_remaining,
                                total - done
                            )
                        } else {
                            stringResource(R.string.Today_achievement_msg_done)
                        },
                    color = OnSurfaceVariant
                )

            }
        }
    }
}

@Preview(
    showBackground = false
)
@Composable
private fun TodayAchievementCardPreview() {
    TodayAchievementCard(
        5,
        1
    )
}