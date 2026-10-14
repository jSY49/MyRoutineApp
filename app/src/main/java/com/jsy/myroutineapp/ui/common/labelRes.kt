package com.jsy.myroutineapp.ui.common

import androidx.annotation.StringRes
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.RoutineCategory

val RoutineCategory.labelRes: Int
    @StringRes
    get() = when (this) {
        RoutineCategory.EXERCISE -> R.string.category_exercise
        RoutineCategory.HEALTH -> R.string.category_health
        RoutineCategory.STUDY -> R.string.category_study
        RoutineCategory.READING -> R.string.category_reading
        RoutineCategory.LIFE -> R.string.category_life
    }
