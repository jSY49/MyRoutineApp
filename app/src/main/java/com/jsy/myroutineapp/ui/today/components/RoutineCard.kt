package com.jsy.myroutineapp.ui.today.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.MultipleDevicePreview
import com.jsy.myroutineapp.ui.common.iconRes
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.SuccessText
import com.jsy.myroutineapp.ui.theme.Surface
import com.jsy.myroutineapp.ui.theme.Typography

@Composable
fun RoutineCard(
    category: RoutineCategory,
    title: String,
    repeatDays: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val color = RoutineTheme.colors.categoryColor(category)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) RoutineTheme.colors.successContainer else Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isChecked) RoutineTheme.colors.successBorder else OutlineVariant
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            //IC
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color.container,
                        RoundedCornerShape(8.dp)
                    )
                    .size(44.dp)
            ) {
                Image(
                    painter = painterResource(category.iconRes),
                    contentDescription = title,
                    colorFilter = ColorFilter.tint(color.main)
                )
            }

            //Contents
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    color = OnSurface,
                    text = title,
                    style = Typography.titleSmall,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    color = if (isChecked) SuccessText else OnSurfaceVariant,
                    text = repeatDays,
                    style = Typography.bodySmall
                )

            }

            //CheckBox
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = RoutineTheme.colors.success,
                    uncheckedColor = RoutineTheme.colors.outlineStrong,
                    checkmarkColor = OnPrimary
                )
            )
        }
    }
}

@MultipleDevicePreview
@Composable
private fun RoutineCardPreview() {
    Column(
        Modifier.background(Color.Transparent)
    ) {

        RoutineCard(
            title = "물 2L 마시기",
            repeatDays = "매일",
            isChecked = true,
            category = RoutineCategory.HEALTH,
            onCheckedChange = {},
            onClick = {}
        )

        Spacer(modifier = Modifier.size(20.dp))
        RoutineCard(
            title = "물 2L 마시기",
            repeatDays = "매일",
            isChecked = false,
            category = RoutineCategory.HEALTH,
            onCheckedChange = {},
            onClick = {}
        )
    }
}