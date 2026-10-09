package com.jsy.myroutineapp.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.ui.theme.Typography
import com.jsy.myroutineapp.ui.navigation.BottomTabs
import com.jsy.myroutineapp.ui.record.RecordScreen
import com.jsy.myroutineapp.ui.statistic.StatisticScreen
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.TextDisabled
import com.jsy.myroutineapp.ui.theme.TextTertiary
import com.jsy.myroutineapp.ui.today.TodayScreen

@Composable
fun MainScreen(
    onAddRoutineClick: () -> Unit = {},
    onRoutineClick: (routineId: Long) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val tabs = listOf(
        BottomTabs.TodayTab,
        BottomTabs.RecordTab,
        BottomTabs.StatisticTab
    )
    var currentTab by remember { mutableStateOf<BottomTabs>(BottomTabs.TodayTab) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = {
                            Image(
                                painter = painterResource(tab.icon),
                                contentDescription = tab.desc,
                                colorFilter = ColorFilter.tint(
                                    if (currentTab == tab) Primary
                                    else TextDisabled
                                )
                            )
                        },
                        label = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(tab.label),
                                    style = Typography.labelSmall,
                                    color = if (currentTab == tab) OnSurface else TextTertiary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(4.dp)
                                        .background(
                                            color = if (currentTab == tab) Primary
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                BottomTabs.TodayTab -> TodayScreen(
                    onAddRoutineClick = onAddRoutineClick,
                    onRoutineClick = onRoutineClick
                )
                BottomTabs.RecordTab -> RecordScreen()
                BottomTabs.StatisticTab -> StatisticScreen()
            }
        }
    }
}

@Preview(
    name = "Phone",
    device = "spec:width=360dp,height=800dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Phone_flip",
    device = "spec:width=360dp,height=880dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MainScreenPreview() {
    MainScreen()
}