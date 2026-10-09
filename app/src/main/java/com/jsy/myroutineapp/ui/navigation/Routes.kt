package com.jsy.myroutineapp.ui.navigation

object Routes {
    const val MAIN = "main"
    const val ADD_ROUTINE = "addRoutine"

    const val ROUTINE_ID_ARG = "routineId"
    const val ROUTINE_DETAIL = "routineDetail/{$ROUTINE_ID_ARG}"

    fun routineDetail(routineId: Long) = "routineDetail/$routineId"
}
