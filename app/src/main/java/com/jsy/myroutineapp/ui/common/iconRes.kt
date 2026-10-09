package com.jsy.myroutineapp.ui.common

import androidx.annotation.DrawableRes
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.RoutineCategory

val RoutineCategory.iconRes: Int
    @DrawableRes
    get() = when (this) {
        RoutineCategory.EXERCISE -> R.drawable.ic_fitness_center_fill
        RoutineCategory.HEALTH -> R.drawable.ic_water_drop_fill
        RoutineCategory.STUDY -> R.drawable.ic_school_fill
        RoutineCategory.READING -> R.drawable.ic_menu_book_fill
        RoutineCategory.LIFE -> R.drawable.ic_self_improvement_fill
    }