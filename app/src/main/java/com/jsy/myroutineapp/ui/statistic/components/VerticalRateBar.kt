package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.Heat2
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.PrimaryContainerHigh
import com.jsy.myroutineapp.ui.theme.TextTertiary
import com.jsy.myroutineapp.ui.theme.Typography

// 0%여도 막대가 완전히 사라지지 않도록 하는 최소 비율
private const val MinBarFraction = 0.02f

/**
 * 최근 7일 달성률 막대 하나.
 * 부모가 준 높이 안에서 [percent]만큼 막대 높이를 채우고, 막대 위에 퍼센트, 아래에 [label]을 표시한다.
 * 높이는 호출하는 쪽에서 지정해야 한다 (예: Modifier.height(120.dp)).
 */
@Composable
fun VerticalRateBar(
    percent: Int,
    label: String,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    val fraction = (percent / 100f).coerceIn(MinBarFraction, 1f)
    val barColor = when {
        isToday -> Primary
        percent == 100 -> Heat2
        else -> PrimaryContainerHigh
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 텍스트 높이를 뺀 나머지 공간을 위(빈 공간) / 아래(막대)로 비율 분배
        if (fraction < 1f) {
            Spacer(modifier = Modifier.weight(1f - fraction))
        }
        Text(
            text = stringResource(R.string.Statistic_week_bar_rate, percent),
            color = if (isToday) Primary else TextTertiary,
            style = Typography.labelMedium,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(fraction)
                .background(color = barColor, shape = RoundedCornerShape(12.dp))
        )
        Text(
            text = label,
            color = if (isToday) Primary else OnSurfaceVariant,
            style = Typography.labelMedium,
            fontWeight = if (isToday) FontWeight.Bold else null,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun VerticalRateBarPreview() {
    VerticalRateBar(
        percent = 30,
        label = "일",
        isToday = true,
        modifier = Modifier
            .width(36.dp)
            .height(160.dp)
    )
}
