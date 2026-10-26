package com.jsy.myroutineapp.ui.statistic.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.IcBox
import com.jsy.myroutineapp.ui.common.iconRes
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnStreakContainer
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.PrimaryTrack
import com.jsy.myroutineapp.ui.theme.RoutineTheme
import com.jsy.myroutineapp.ui.theme.StreakContainer
import com.jsy.myroutineapp.ui.theme.TextTertiary
import com.jsy.myroutineapp.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate

/** 루틴별 연속 달성 한 줄에 표시할 데이터 */
data class RoutineStreak(
    val routine: Routine,
    /** 현재 연속 달성 일수 */
    val currentStreak: Int,
    /** 역대 최고 연속 달성 일수 */
    val bestStreak: Int,
    /** 최근 수행 기록 (오래된 순, true = 완료) */
    val recent: List<Boolean>
)

@Composable
fun RoutineStreakCard(
    streaks: List<RoutineStreak>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .background(color = OnPrimary, shape = shape)
            .border(border = BorderStroke(1.dp, OutlineVariant), shape = shape)
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.Statistic_streak_title),
                color = OnSurface,
                style = Typography.titleSmall,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = stringResource(R.string.Statistic_streak_desc),
                color = OnSurfaceVariant,
                style = Typography.labelMedium,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            streaks.forEach { streak ->
                RoutineStreakRow(streak)
            }
        }
    }
}

@Composable
private fun RoutineStreakRow(
    streak: RoutineStreak,
    modifier: Modifier = Modifier
) {
    val category = streak.routine.category
    val color = RoutineTheme.colors.categoryColor(category)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IcBox(color, category.iconRes, streak.routine.title)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = streak.routine.title,
                    color = OnSurface,
                    style = Typography.labelLarge,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = stringResource(R.string.Statistic_streak_best, streak.bestStreak),
                    color = TextTertiary,
                    style = Typography.labelSmall,
                    maxLines = 1
                )
            }
            // 칸 수가 달라져도 폭에 맞게 균등 분할
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                streak.recent.forEach { done ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (done) color.main else PrimaryTrack,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }

        StreakBadge(days = streak.currentStreak)
    }
}

@Composable
private fun StreakBadge(days: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .background(color = StreakContainer, shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_local_fire_department_fill),
            contentDescription = null,
            tint = OnStreakContainer,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = stringResource(R.string.Statistic_streak_badge_days, days),
            color = OnStreakContainer,
            style = Typography.labelMedium
        )
    }
}

@Preview(
    name = "Phone",
    showBackground = true
)
@Composable
private fun RoutineStreakCardPreview() {
    fun routine(id: Long, title: String, category: RoutineCategory) = Routine(
        id = id,
        title = title,
        category = category,
        repeatDays = DayOfWeek.entries.toSet(),
        description = "",
        startDate = LocalDate.of(2026, 10, 1)
    )

    RoutineStreakCard(
        streaks = listOf(
            RoutineStreak(
                routine(1, "물 2L 마시기", RoutineCategory.HEALTH), 23, 23,
                List(7) { true }
            ),
            RoutineStreak(
                routine(2, "운동 30분", RoutineCategory.EXERCISE), 3, 6,
                listOf(true, true, true, false, true, true, true)
            ),
            RoutineStreak(
                routine(3, "영어 공부 20분", RoutineCategory.STUDY), 6, 16,
                listOf(false, true, true, true, true, true, true)
            ),
            RoutineStreak(
                routine(4, "책 읽기 30분", RoutineCategory.READING), 5, 9,
                listOf(false, false, true, true, true, true, true)
            )
        )
    )
}
