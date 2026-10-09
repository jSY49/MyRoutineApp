package com.jsy.myroutineapp.ui.common

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

fun Set<DayOfWeek>.toRepeatText(): String = when {
    size == 7 -> "매일"
    isEmpty() -> "반복 없음"
    else -> sorted().joinToString(" ") {
        it.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
    }
}