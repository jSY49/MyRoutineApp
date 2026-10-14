package com.jsy.myroutineapp.ui.addRoutine.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.iconRes
import com.jsy.myroutineapp.ui.common.labelRes
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.Typography

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySection(
    selected: RoutineCategory?,
    onSelect: (RoutineCategory) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxWidth()) {

        // Title
        Text(
            text = stringResource(R.string.Add_input_cate),
            style = Typography.labelLarge,
            color = OnSurface
        )

        Spacer(Modifier.height(12.dp))

        // Chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoutineCategory.entries.forEach { category ->
                CategoryChip(
                    category = category,
                    selected = category == selected,
                    onClick = { onSelect(category) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.Add_input_cate_desc),
            style = Typography.bodySmall,
            color = OnSurfaceVariant
        )

    }
}

@Composable
fun CategoryChip(
    category: RoutineCategory,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = RoutineTheme.colors.categoryColor(category)

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(category.labelRes),
                style = Typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(category.iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        shape = RoundedCornerShape(12.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outlineVariant,
            selectedBorderColor = color.main,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = OnSurface,
            iconColor = color.main,
            selectedContainerColor = color.container,
            selectedLabelColor = OnSurface,
            selectedLeadingIconColor = color.main
        ),
        modifier = modifier.height(44.dp)
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun CategorySectionPreview() {
    var selected by remember { mutableStateOf<RoutineCategory?>(RoutineCategory.EXERCISE) }

    CategorySection(
        selected = selected,
        onSelect = { selected = it }
    )
}
