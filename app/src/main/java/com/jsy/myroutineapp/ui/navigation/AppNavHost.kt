package com.jsy.myroutineapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jsy.myroutineapp.ui.addRoutine.AddRoutineScreen
import com.jsy.myroutineapp.ui.main.MainScreen
import com.jsy.myroutineapp.ui.routineDetail.RoutineDetailScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
        composable(Routes.MAIN) {
            MainScreen(
                onAddRoutineClick = { navController.navigate(Routes.ADD_ROUTINE) },
                onRoutineClick = { routineId ->
                    navController.navigate(Routes.routineDetail(routineId))
                }
            )
        }

        composable(Routes.ADD_ROUTINE) {
            AddRoutineScreen()
        }

        composable(
            route = Routes.ROUTINE_DETAIL,
            arguments = listOf(
                navArgument(Routes.ROUTINE_ID_ARG) { type = NavType.LongType }
            )
        ) {
            RoutineDetailScreen()
        }
    }

}
