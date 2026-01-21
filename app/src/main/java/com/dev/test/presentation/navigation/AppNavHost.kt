package com.dev.test.presentation.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coop.feature_goals.presentation.GoalSavingsDashboardScreen
import com.dev.test.presentation.navigation.AppRoute
import com.dev.test.presentation.screens.creategoals.CreateGoalScreen2
import com.dev.test.presentation.screens.deposit.DepositScreen2
import com.dev.test.presentation.screens.mygoals.StartSavingScreen
import com.dev.test.presentation.screens.withdraw.WithdrawScreen2


@Composable
fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController = navHostController, startDestination = AppRoute.GoalSavingsDashboardScreen.route, modifier = modifier) {


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
            CreateGoalScreen2(navHostController)
        }
        composable(AppRoute.DepositScreen.route) {
            DepositScreen2(navHostController)

        }
        composable(AppRoute.WithdrawScreen.route) {
            WithdrawScreen2 (navHostController)
        }


    }

}