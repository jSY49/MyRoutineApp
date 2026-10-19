package com.jsy.myroutineapp.ui.record.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.ui.record.util.levelColor
import com.jsy.myroutineapp.ui.record.util.levelTextColor
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.Typography

@Composable
fun CalendarDayCell(
    day: Int,
    rate: Float,
    isSelected: Boolean,
    isToday: Boolean,
    isFuture: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = modifier.aspectRatio(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 원: 남은 높이를 모두 쓰고, 높이를 기준으로 정사각형 유지
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(
                        1f,
                        matchHeightConstraintsFirst = true
                    )
                    .clip(CircleShape)
                    .background(if (isFuture) Color.Transparent else levelColor(rate))
                    .then(
                        if (isSelected) Modifier.border(
                            BorderStroke(
                                2.dp,
                                Primary
                            ),
                            CircleShape
                        )
                        else Modifier
                    )
                    .clickable(
                        enabled = !isFuture,
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    style = Typography.bodyMedium,
                    color = if (isFuture) OnSurfaceVariant.copy(alpha = 0.4f) else levelTextColor(rate)
                )
            }

            Spacer(Modifier.height(3.dp)) // 원과 점 사이 간격

            // 점 자리: 오늘이 아니어도 높이를 유지해서 셀 정렬이 안 흐트러지게 함
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (isToday) Primary else Color.Transparent)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun CalendarDayCellPreview() {
    CalendarDayCell(
        day = 1,
        rate = 0.4f,
        isSelected = false,
        isToday = true,
        isFuture = false,
        onClick = {},
        modifier = Modifier.size(40.dp)
    )
}