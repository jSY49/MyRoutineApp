package com.jsy.myroutineapp.ui.addRoutine.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import com.jsy.myroutineapp.ui.common.RepeatPreset
import com.jsy.myroutineapp.ui.common.toRepeatText
import com.jsy.myroutineapp.ui.theme.Error
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnPrimaryContainer
import com.jsy.myroutineapp.ui.theme.PrimaryContainer
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RepeatDaysSection(
    selected: Set<DayOfWeek>,
    onSelect: (DayOfWeek) -> Unit,
    onSelectPreset: (Set<DayOfWeek>) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxWidth()) {

        // Title + Presets
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Add_input_date_title),
                style = Typography.labelLarge,
                color = OnSurface
            )
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                RepeatPreset.entries.forEach { preset ->
                    RepeatPresetChip(
                        text = preset.label,
                        selected = selected == preset.days,
                        onClick = { onSelectPreset(preset.days) }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            DayOfWeek.entries.forEach { day ->
                RepeatDayChip(
                    day = day,
                    selected = day in selected,
                    onClick = { onSelect(day) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "주 ${selected.size}일 · ${selected.toRepeatText()}",
            style = Typography.bodySmall,
            color = OnSurfaceVariant
        )

    }
}

@Composable
private fun RepeatPresetChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) PrimaryContainer else Color.Transparent,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = Typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) OnPrimaryContainer else OnSurfaceVariant,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun RepeatDayChip(
    day: DayOfWeek,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                style = Typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        shape = CircleShape,
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outlineVariant,
            selectedBorderColor = Primary,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = if(day == DayOfWeek.SUNDAY) Error else OnSurface,
            selectedContainerColor = Primary,
            selectedLabelColor = OnPrimary,
        ),
        modifier = modifier.aspectRatio(1f)
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun RepeatDaysSectionPreview() {

    RepeatDaysSection(
        selected = DayOfWeek.entries.toSet(),
        onSelect = {},
        onSelectPreset = {}
    )
}
