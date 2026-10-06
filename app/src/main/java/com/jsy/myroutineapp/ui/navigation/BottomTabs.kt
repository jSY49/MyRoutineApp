package com.jsy.myroutineapp.ui.navigation

import com.jsy.myroutineapp.R

sealed class BottomTabs(
    val label : Int ,
    val icon : Int,
    val desc : String
) {

    object TodayTab : BottomTabs(
        label = R.string.BottomTab_label_today,
        icon = R.drawable.ic_task_alt,
        desc = "Today Tab"
    )

    object RecordTab : BottomTabs(
        label = R.string.BottomTab_label_record,
        icon = R.drawable.ic_calendar_month,
        desc = "Record Tab"
    )

    object StatisticTab : BottomTabs(
        label = R.string.BottomTab_label_statistic,
        icon = R.drawable.ic_insert_chart,
        desc = "Statistic Tab"
    )
}