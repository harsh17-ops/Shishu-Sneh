package com.shishusneh.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shishusneh.app.ui.components.AppScaffold
import com.shishusneh.app.ui.screens.advanced.AppointmentsScreen
import com.shishusneh.app.ui.screens.advanced.EmergencyGuidanceScreen
import com.shishusneh.app.ui.screens.advanced.FamilyAccessScreen
import com.shishusneh.app.ui.screens.advanced.SmartCareScreen
import com.shishusneh.app.ui.screens.feeding.FeedingGuideScreen
import com.shishusneh.app.ui.screens.growth.GrowthScreen
import com.shishusneh.app.ui.screens.home.DashboardScreen
import com.shishusneh.app.ui.screens.milestone.MilestonesScreen
import com.shishusneh.app.ui.screens.settings.ProfileEditorScreen
import com.shishusneh.app.ui.screens.settings.SettingsScreen
import com.shishusneh.app.ui.screens.vaccine.VaccinesScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            AppScaffold(
                navController = navController,
                title = "Dashboard",
                showBottomBar = true,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                DashboardScreen(
                    paddingValues = padding,
                    onOpenFeedingGuide = { navController.navigate(Screen.FeedingGuide.route) },
                    onOpenSmartCare = { navController.navigate(Screen.SmartCare.route) }
                )
            }
        }
        composable(Screen.Growth.route) {
            AppScaffold(
                navController = navController,
                title = "Growth",
                showBottomBar = true,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                GrowthScreen(paddingValues = padding)
            }
        }
        composable(Screen.Vaccines.route) {
            AppScaffold(
                navController = navController,
                title = "Vaccination Schedule",
                showBottomBar = true,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                VaccinesScreen(paddingValues = padding)
            }
        }
        composable(Screen.Milestones.route) {
            AppScaffold(
                navController = navController,
                title = "Milestones",
                showBottomBar = true,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                MilestonesScreen(paddingValues = padding)
            }
        }
        composable(Screen.FeedingGuide.route) {
            AppScaffold(
                navController = navController,
                title = "Feeding Guide",
                showBottomBar = false,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                FeedingGuideScreen(paddingValues = padding)
            }
        }
        composable(Screen.Settings.route) {
            AppScaffold(
                navController = navController,
                title = "Settings",
                showBottomBar = false,
                onSettingsClick = { }
            ) { padding ->
                SettingsScreen(
                    paddingValues = padding,
                    onEditProfile = { navController.navigate(Screen.Profile.route) },
                    onOpenSmartCare = { navController.navigate(Screen.SmartCare.route) },
                    onOpenAppointments = { navController.navigate(Screen.Appointments.route) },
                    onOpenFamilyAccess = { navController.navigate(Screen.FamilyAccess.route) },
                    onOpenEmergency = { navController.navigate(Screen.Emergency.route) }
                )
            }
        }
        composable(Screen.Profile.route) {
            AppScaffold(
                navController = navController,
                title = "Edit Profile",
                showBottomBar = false,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                androidx.compose.foundation.layout.Box(modifier = Modifier.padding(padding)) {
                    ProfileEditorScreen(onSaved = { navController.popBackStack() })
                }
            }
        }
        composable(Screen.SmartCare.route) {
            AppScaffold(
                navController = navController,
                title = "Smart Care",
                showBottomBar = false,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                SmartCareScreen(paddingValues = padding)
            }
        }
        composable(Screen.Appointments.route) {
            AppScaffold(
                navController = navController,
                title = "Appointments",
                showBottomBar = false,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                AppointmentsScreen(paddingValues = padding)
            }
        }
        composable(Screen.FamilyAccess.route) {
            AppScaffold(
                navController = navController,
                title = "Family Access",
                showBottomBar = false,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                FamilyAccessScreen(paddingValues = padding)
            }
        }
        composable(Screen.Emergency.route) {
            AppScaffold(
                navController = navController,
                title = "Emergency Help",
                showBottomBar = false,
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            ) { padding ->
                EmergencyGuidanceScreen(paddingValues = padding)
            }
        }
    }
}
