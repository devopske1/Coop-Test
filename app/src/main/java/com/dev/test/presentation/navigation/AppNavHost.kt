package com.dev.test.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev.test.presentation.screens.creategoals.CreateGoalScreen
import com.dev.test.presentation.screens.creategoals.CreateGoalViewModel
import com.dev.test.presentation.screens.dashboard.StartSavingScreen
import com.dev.test.presentation.screens.deposit.DepositScreen
import com.dev.test.presentation.screens.mygoals.GoalSavingsDashboardScreen
import com.dev.test.presentation.screens.withdraw.WithdrawScreen


@Composable
fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController = navHostController, startDestination = AppRoute.StartSavingScreen.route, modifier = modifier) {


        composable(AppRoute.StartSavingScreen.route) {
            StartSavingScreen(navHostController)
        }
        composable(AppRoute.GoalSavingsDashboardScreen.route) {
            GoalSavingsDashboardScreen(navHostController)

        }
        composable(AppRoute.GoalSavingsDashboardScreen.route) {
            GoalSavingsDashboardScreen(navHostController)

        }
        composable(AppRoute.CreateGoalScreen.route) {
            val viewModel: CreateGoalViewModel = hiltViewModel()
            CreateGoalScreen(navHostController, viewModel)
        }
        composable(AppRoute.DepositScreen.route) {
            DepositScreen(navHostController)

        }
        composable(AppRoute.WithdrawScreen.route) {
            WithdrawScreen (navHostController)
        }


    }

}