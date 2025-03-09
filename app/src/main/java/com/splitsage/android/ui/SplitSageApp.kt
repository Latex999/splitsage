package com.splitsage.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.splitsage.android.R
import com.splitsage.android.ui.navigation.Screen
import com.splitsage.android.ui.screens.activity.ActivityScreen
import com.splitsage.android.ui.screens.expenses.AddExpenseScreen
import com.splitsage.android.ui.screens.expenses.ExpensesScreen
import com.splitsage.android.ui.screens.groups.GroupsScreen
import com.splitsage.android.ui.screens.home.HomeScreen
import com.splitsage.android.ui.screens.profile.ProfileScreen

@Composable
fun SplitSageApp() {
    val navController = rememberNavController()
    
    val items = listOf(
        Screen.Home,
        Screen.Groups,
        Screen.Expenses,
        Screen.Activity,
        Screen.Profile
    )
    
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            // Only show bottom navigation for main screens
            val isMainScreen = currentDestination?.route in items.map { it.route }
            
            if (isMainScreen) {
                NavigationBar {
                    items.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = stringResource(id = screen.resourceId)
                                )
                            },
                            label = { Text(stringResource(id = screen.resourceId)) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
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
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Groups.route) {
                GroupsScreen(navController = navController)
            }
            composable(Screen.Expenses.route) {
                ExpensesScreen(navController = navController)
            }
            composable(Screen.Activity.route) {
                ActivityScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            
            // Additional screens not in the bottom navigation
            composable(Screen.AddExpense.route) {
                AddExpenseScreen(navController = navController)
            }
        }
    }
}