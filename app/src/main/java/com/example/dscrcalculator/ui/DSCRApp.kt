package com.example.dscrcalculator.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.dscrcalculator.ui.calculator.CalculatorScreen
import com.example.dscrcalculator.ui.history.HistoryDetailScreen
import com.example.dscrcalculator.ui.history.HistoryScreen
import com.example.dscrcalculator.ui.navigation.Screen

@Composable
fun DSCRApp() {
    val navController = rememberNavController()
    val screens = listOf(Screen.Calculator, Screen.History)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val shouldShowBottomBar = screens.any { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.titleResId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calculator.route) {
                CalculatorScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    onCalculationClick = { calculationId ->
                        navController.navigate(Screen.historyDetailRoute(calculationId))
                    }
                )
            }
            composable(
                route = Screen.HISTORY_DETAIL_ROUTE,
                arguments = listOf(
                    navArgument(Screen.HISTORY_DETAIL_ARG) {
                        type = NavType.IntType
                    }
                )
            ) {
                HistoryDetailScreen(onBackClick = navController::navigateUp)
            }
        }
    }
}
