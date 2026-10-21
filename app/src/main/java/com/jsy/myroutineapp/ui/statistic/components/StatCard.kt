package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.Typography

// 프로그레스바(트랙 4dp + 위아래 6dp) 높이. middle 슬롯이 비어도 이 높이는 유지된다.
private val StatCardMiddleHeight = 16.dp

/**
 * 통계 요약 카드 공용 레이아웃.
 * label / value / middle / desc 위치가 카드마다 동일하게 유지된다.
 */
@Composable
fun StatCard(
    label: String,
    desc: String,
    modifier: Modifier = Modifier,
    middle: @Composable () -> Unit = {},
    value: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .background(color = OnPrimary, shape = shape)
            .border(border = BorderStroke(1.dp, OutlineVariant), shape = shape)
            .padding(18.dp)
    ) {
        Text(
            text = label,
            color = OnSurfaceVariant,
            style = Typography.labelMedium,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        Box(modifier = Modifier.padding(vertical = 4.dp)) {
            value()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(StatCardMiddleHeight),
            contentAlignment = Alignment.Center
        ) {
            middle()
        }
        Text(
            text = desc,
            color = OnSurfaceVariant,
            style = Typography.labelSmall,
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}

@Composable
fun StatCardValue(text: String) {
    Text(
        text = text,
        color = OnSurface,
        style = Typography.displaySmall,
        modifier = Modifier.fillMaxWidth()
    )
}
