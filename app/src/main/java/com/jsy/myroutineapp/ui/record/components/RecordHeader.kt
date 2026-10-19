package com.jsy.myroutineapp.ui.record.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RecordHeader(
    modifier: Modifier = Modifier,
    onTodayClick: () -> Unit = {},
) {

    val formatter = DateTimeFormatter.ofPattern(
        "MM월 d일 E요일",
        Locale.KOREA
    )
    val text = LocalDateTime.now()
        .format(formatter)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(R.string.Record_title),
            style = Typography.headlineMedium,
            color = OnSurface,
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onTodayClick)
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_today),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(R.string.Record_today_button), // "오늘" 문자열 리소스 추가
                style = Typography.labelLarge,
                color = Primary
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TodayHeaderPreview() {
    RecordHeader()
}