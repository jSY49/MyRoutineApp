package com.jsy.myroutineapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.ui.theme.CategoryColor

@Composable
fun IcBox(color: CategoryColor, iconRes: Int, title: String) {
    Box(
        contentAlignment = Center,
        modifier = Modifier
            .background(
                color.container,
                RoundedCornerShape(8.dp)
            )
            .size(44.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = title,
            colorFilter = ColorFilter.tint(color.main)
        )
    }
}
