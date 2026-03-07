package com.pocketbrain.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.pocketbrain.ui.screens.*

sealed class Screen(val route: String, val labelFr: String, val icon: ImageVector) {
    object Dashboard  : Screen("dashboard",  "Tableau",    Icons.Filled.Home)
    object History    : Screen("history",    "Historique", Icons.Filled.History)
    object Charts     : Screen("charts",     "Graphiques", Icons.Filled.BarChart)
    object Reports    : Screen("reports",    "Rapports",   Icons.Filled.PictureAsPdf)
    object Settings   : Screen("settings",   "Réglages",   Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    Screen.Dashboard, Screen.History, Screen.Charts, Screen.Reports, Screen.Settings
)

@Composable
fun PocketBrainNavGraph() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val current = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon     = { Icon(screen.icon, contentDescription = screen.labelFr) },
                        label    = { Text(screen.labelFr) },
                        selected = current?.hierarchy?.any { it.route == screen.route } == true,
                        onClick  = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Dashboard.route,
            modifier         = Modifier.padding(innerPadding),
            enterTransition  = { slideInHorizontally(tween(250)) + fadeIn(tween(250)) },
            exitTransition   = { slideOutHorizontally(tween(250)) + fadeOut(tween(250)) }
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.History.route)   { HistoryScreen() }
            composable(Screen.Charts.route)    { ChartsScreen() }
            composable(Screen.Reports.route)   { ReportsScreen() }
            composable(Screen.Settings.route)  { SettingsScreen() }
        }
    }
}
