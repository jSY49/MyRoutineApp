package com.jsy.myroutineapp.ui.common

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

enum class RepeatPreset(val label: String, val days: Set<DayOfWeek>) {
    EVERYDAY("매일", DayOfWeek.entries.toSet()),
    WEEKDAYS(
        "평일",
        setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        )
    ),
    WEEKENDS("주말", setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
}

fun Set<DayOfWeek>.toRepeatText(): String =
    if (isEmpty()) "반복 없음"
    else RepeatPreset.entries.firstOrNull { it.days == this }?.label
        ?: sorted().joinToString(" ") { it.getDisplayName(TextStyle.SHORT, Locale.KOREAN) }
