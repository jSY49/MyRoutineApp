package com.jsy.myroutineapp.ui.record.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.ui.theme.*

@Composable
fun CalendarLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box10(
                Modifier.border(
                    BorderStroke(
                        2.dp,
                        Primary
                    ),
                    CircleShape
                )
            )
            Text(
                stringResource(R.string.Record_legend_selected),
                style = Typography.labelSmall,
                color = OnSurfaceVariant
            )
            Spacer(Modifier.width(6.dp))
            Box10(
                Modifier
                    .size(4.dp)
                    .background(
                        Primary,
                        CircleShape
                    )
            )
            Text(
                stringResource(R.string.Record_legend_today),
                style = Typography.labelSmall,
                color = OnSurfaceVariant
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "${stringResource(R.string.Record_legend_rate_min)} 0%",
                style = Typography.labelSmall,
                color = OnSurfaceVariant
            )
            listOf(
                Heat0,
                Heat1,
                Heat2,
                Heat3
            ).forEach {
                Box10(
                    Modifier.background(
                        it,
                        CircleShape
                    )
                )
            }
            Text(
                stringResource(R.string.Record_legend_rate_max),
                style = Typography.labelSmall,
                color = OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun Box10(modifier: Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier
            .size(10.dp)
            .clip(CircleShape)
    )
}