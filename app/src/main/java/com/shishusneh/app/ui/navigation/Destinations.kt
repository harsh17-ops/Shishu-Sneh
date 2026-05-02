package com.shishusneh.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Vaccines
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Growth : Screen("growth")
    data object Vaccines : Screen("vaccines")
    data object Milestones : Screen("milestones")
    data object FeedingGuide : Screen("feeding")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, "Home", Icons.Rounded.Home),
    BottomNavItem(Screen.Growth.route, "Growth", Icons.Rounded.MonitorWeight),
    BottomNavItem(Screen.Vaccines.route, "Vaccines", Icons.Rounded.Vaccines),
    BottomNavItem(Screen.Milestones.route, "Milestones", Icons.Rounded.Flag)
)
