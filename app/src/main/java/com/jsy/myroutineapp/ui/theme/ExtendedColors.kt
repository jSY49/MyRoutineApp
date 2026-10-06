package com.jsy.myroutineapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CategoryColor(val main: Color, val container: Color)

@Immutable
data class ExtendedColors(
    val success: Color = Success,
    val successText: Color = SuccessText,
    val successContainer: Color = SuccessContainer,
    val successContainerHigh: Color = SuccessContainerHigh,
    val successBorder: Color = SuccessBorder,
    val successOnInverse: Color = SuccessOnInverse,
    val errorOutline: Color = ErrorOutline,
    val primarySoft: Color = PrimarySoft,
    val primaryText: Color = PrimaryText,
    val primaryTrack: Color = PrimaryTrack,
    val outlineStrong: Color = OutlineStrong,
    val textTertiary: Color = TextTertiary,
    val textDisabled: Color = TextDisabled,
    val heat: List<Color> = listOf(Heat0, Heat1, Heat2, Heat3),
    val onHeat: List<Color> = listOf(OnHeat0, OnHeat1, OnHeat2, OnHeat3),
    val exercise: CategoryColor = CategoryColor(Exercise, ExerciseContainer),
    val health: CategoryColor = CategoryColor(Health, HealthContainer),
    val study: CategoryColor = CategoryColor(Study, StudyContainer),
    val reading: CategoryColor = CategoryColor(Reading, ReadingContainer),
    val life: CategoryColor = CategoryColor(Life, LifeContainer),
)

val LocalExtendedColors = staticCompositionLocalOf { ExtendedColors() }

object RoutineTheme {
    val colors: ExtendedColors
        @Composable @ReadOnlyComposable get() = LocalExtendedColors.current
}
