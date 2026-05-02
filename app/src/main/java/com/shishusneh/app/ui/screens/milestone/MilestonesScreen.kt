package com.shishusneh.app.ui.screens.milestone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.EmptyStateView
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.viewmodel.MilestoneViewModel

@Composable
fun MilestonesScreen(
    paddingValues: PaddingValues,
    viewModel: MilestoneViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.loading) {
        LoadingView()
        return
    }

    if (state.milestones.isEmpty()) {
        EmptyStateView(title = "No milestones yet", subtitle = "Create the baby profile to preload the milestone checklist.")
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.milestones, key = { it.id }) { milestone ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = milestone.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = milestone.description)
                    Text(text = "Expected around ${milestone.expectedAgeMonths} months")
                    Switch(
                        checked = milestone.isAchieved,
                        onCheckedChange = { viewModel.updateMilestone(milestone, it) }
                    )
                }
            }
        }
    }
}
