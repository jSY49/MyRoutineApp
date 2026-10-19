package com.jsy.myroutineapp.ui.record.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface as M3Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.IcBox
import com.jsy.myroutineapp.ui.common.MultipleDevicePreview
import com.jsy.myroutineapp.ui.common.iconRes
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.SurfaceContainer
import com.jsy.myroutineapp.ui.theme.Typography
import com.jsy.myroutineapp.ui.theme.Surface

@Composable
fun RecordRoutineItem(
    category: RoutineCategory,
    title: String,
    repeatText: String,
    isDone: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = RoutineTheme.colors.categoryColor(category)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, OutlineVariant),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IcBox(color, category.iconRes, title)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = Typography.titleSmall,
                    color = OnSurface
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = repeatText,
                    style = Typography.bodySmall,
                    color = OnSurfaceVariant
                )
            }

            StatusChip(isDone = isDone, onClick = onToggle)
        }
    }
}

@Composable
private fun StatusChip(
    isDone: Boolean,
    onClick: () -> Unit
) {
    val extended = RoutineTheme.colors
    val container = if (isDone) extended.successContainerHigh else SurfaceContainer
    val content = if (isDone) extended.successText else OnSurfaceVariant

    M3Surface(
        shape = CircleShape,
        color = container,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isDone) R.drawable.ic_check_circle_fill
                    else R.drawable.ic_radio_button_unchecked
                ),
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(
                    if (isDone) R.string.Record_status_done else R.string.Record_status_todo
                ),
                style = Typography.labelMedium,
                color = content
            )
        }
    }
}

@MultipleDevicePreview
@Composable
private fun RecordRoutineItemPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecordRoutineItem(
            category = RoutineCategory.HEALTH,
            title = "물 2L 마시기",
            repeatText = "매일",
            isDone = true,
            onToggle = {}
        )
        RecordRoutineItem(
            category = RoutineCategory.EXERCISE,
            title = "운동 30분",
            repeatText = "월 · 화 · 목",
            isDone = false,
            onToggle = {}
        )
    }
}
