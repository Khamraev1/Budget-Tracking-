package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AddExpenseScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExpenseListScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatisticsScreen

@Composable
fun BudgetSyncApp(
    viewModel: BudgetViewModel = viewModel(factory = BudgetViewModel.Factory())
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentRoute != "add") {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "home") Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == "home",
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            if (currentRoute != "home") {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "history") Icons.AutoMirrored.Filled.List else Icons.AutoMirrored.Outlined.List, contentDescription = "History") },
                        label = { Text("History") },
                        selected = currentRoute == "history",
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            if (currentRoute != "history") {
                                navController.navigate("history") {
                                    popUpTo("home")
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "statistics") Icons.Filled.PieChart else Icons.Outlined.PieChart, contentDescription = "Statistics") },
                        label = { Text("Stats") },
                        selected = currentRoute == "statistics",
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            if (currentRoute != "statistics") {
                                navController.navigate("statistics") {
                                    popUpTo("home")
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(if (currentRoute == "settings") Icons.Filled.Settings else Icons.Outlined.Settings, contentDescription = "Setup") },
                        label = { Text("Setup") },
                        selected = currentRoute == "settings",
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            if (currentRoute != "settings") {
                                navController.navigate("settings") {
                                    popUpTo("home")
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { DashboardScreen(viewModel, onNavigateToAdd = { navController.navigate("add") }) }
            composable("history") { ExpenseListScreen(viewModel) }
            composable("statistics") { StatisticsScreen(viewModel) }
            composable("settings") { SettingsScreen(viewModel) }
            composable("add") { AddExpenseScreen(viewModel, onBack = { navController.popBackStack() }) }
        }
    }
}
