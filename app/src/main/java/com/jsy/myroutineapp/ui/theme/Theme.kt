package com.jsy.myroutineapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    // 선택 칩·tonal 버튼 = 연보라
    secondary = Primary,
    onSecondary = OnPrimary,
    secondaryContainer = PrimaryContainer,
    onSecondaryContainer = OnPrimaryContainer,

    tertiary = Streak,
    onTertiary = OnPrimary,
    tertiaryContainer = StreakContainer,
    onTertiaryContainer = OnStreakContainer,

    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = Primary,                 // tonalElevation 0이면 영향 없음
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    inversePrimary = PrimaryContainerHigh,

    error = Error,
    onError = OnPrimary,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorText,

    outline = Outline,
    outlineVariant = OutlineVariant,
    scrim = Color(0xFF141420),             // 불투명하게

    surfaceBright = Surface,
    surfaceDim = SurfaceDisabled,          // 가장 어두운 면이어야 함
    surfaceContainerLowest = Surface,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceDisabled,
)
// 다크 모드 디자인이 아직 없어 라이트 모드와 동일하게 사용
private val DarkColorScheme = LightColorScheme

@Composable
fun MyRoutineAppTheme(
    // 다크 모드 디자인이 없으므로 항상 라이트 팔레트 사용
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalExtendedColors provides ExtendedColors()) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}
