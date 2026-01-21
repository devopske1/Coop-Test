package com.dev.test.presentation.navigation



sealed class AppRoute(val route: String) {
    object StartSavingScreen : AppRoute("start_saving")
    object GoalSavingsDashboardScreen : AppRoute("goals_savings")
    object CreateGoalScreen : AppRoute("create_goal")
    object DepositScreen : AppRoute("deposit")
    object WithdrawScreen : AppRoute("withdraw")
}