package com.shishusneh.app.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.DualMetricRow
import com.shishusneh.app.ui.components.EmptyStateView
import com.shishusneh.app.ui.components.HeroCard
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.viewmodel.DashboardViewModel
import com.shishusneh.app.utils.DateUtils

@Composable
fun DashboardScreen(
    paddingValues: PaddingValues,
    onOpenFeedingGuide: () -> Unit,
    onOpenSmartCare: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.loading) {
        LoadingView()
        return
    }

    val snapshot = state.snapshot
    if (snapshot?.babyProfile == null) {
        EmptyStateView(title = "Profile needed", subtitle = "Create the baby profile to unlock the dashboard.")
        return
    }
    val language = LocalConfiguration.current.locales[0]?.language ?: "en"
    val feedingTitle = if (language == "hi") snapshot.feedingTip?.titleHi else snapshot.feedingTip?.titleEn
    val feedingContent = if (language == "hi") snapshot.feedingTip?.contentHi else snapshot.feedingTip?.contentEn

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HeroCard(
            title = "Hello, ${snapshot.babyProfile.name}",
            subtitle = "Tracking healthy growth through the first year",
            value = DateUtils.ageLabel(snapshot.babyProfile.dobMillis)
        )
        DualMetricRow(
            leftTitle = "Latest weight",
            leftValue = snapshot.latestWeight?.let { "${it.weightKg} kg" } ?: "Add first entry",
            rightTitle = "Next vaccine",
            rightValue = snapshot.nextVaccination?.name ?: "All done"
        )
        DualMetricRow(
            leftTitle = "Milestone progress",
            leftValue = "${snapshot.milestoneProgress}%",
            rightTitle = "Feeding tip",
            rightValue = feedingTitle ?: "Daily guidance ready"
        )
        Text(
            text = "Daily feeding insight",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = feedingContent ?: "Feeding tips will appear based on your baby's age.",
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onOpenFeedingGuide) {
            Text("Open Feeding Guide")
        }
        Button(onClick = onOpenSmartCare) {
            Text("Open Smart Care Hub")
        }
    }
}
