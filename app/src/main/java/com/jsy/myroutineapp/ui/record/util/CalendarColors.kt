package com.jsy.myroutineapp.ui.record.util

import androidx.compose.ui.graphics.Color
import com.jsy.myroutineapp.ui.theme.Heat0
import com.jsy.myroutineapp.ui.theme.Heat1
import com.jsy.myroutineapp.ui.theme.Heat2
import com.jsy.myroutineapp.ui.theme.Heat3
import com.jsy.myroutineapp.ui.theme.OnSurface

fun levelColor(rate: Float): Color = when {
    rate <= 0f -> Heat0
    rate < 0.5f -> Heat1
    rate < 1f -> Heat2
    else -> Heat3
}

fun levelTextColor(rate: Float): Color =
    if (rate >= 1f) Color.White else OnSurface