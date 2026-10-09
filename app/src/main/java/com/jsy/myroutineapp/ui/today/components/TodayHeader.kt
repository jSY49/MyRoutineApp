package com.jsy.myroutineapp.ui.today.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun TodayHeader(modifier: Modifier = Modifier) {

    val formatter = DateTimeFormatter.ofPattern(
        "MM월 d일 E요일",
        Locale.KOREA
    )
    val text = LocalDateTime.now()
        .format(formatter)

    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp)
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium,
            color = Primary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = stringResource(R.string.Today_header_body),
            style = Typography.headlineMedium,
            color = OnSurface,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = stringResource(R.string.Today_header_desc),
            color = OnSurfaceVariant,
            style = Typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TodayHeaderPreview() {
    TodayHeader()
}