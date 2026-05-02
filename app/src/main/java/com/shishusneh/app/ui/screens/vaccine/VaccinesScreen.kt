package com.shishusneh.app.ui.screens.vaccine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.EmptyStateView
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.ui.theme.SoftRed
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.viewmodel.VaccineViewModel

@Composable
fun VaccinesScreen(
    paddingValues: PaddingValues,
    viewModel: VaccineViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.loading) {
        LoadingView()
        return
    }

    if (state.vaccines.isEmpty()) {
        EmptyStateView(title = "No vaccines loaded", subtitle = "Create the baby profile to generate the schedule.")
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.vaccines, key = { it.id }) { vaccine ->
            val overdue = !vaccine.isCompleted && vaccine.dueDateMillis < System.currentTimeMillis()
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (overdue) SoftRed.copy(alpha = 0.15f) else Color.Transparent)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = vaccine.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "Protects against ${vaccine.diseasePrevented}")
                    Text(text = "Due: ${DateUtils.formatDate(vaccine.dueDateMillis)}")
                    Text(
                        text = when {
                            vaccine.isCompleted -> "Completed"
                            overdue -> "Overdue"
                            else -> "Pending"
                        },
                        color = if (overdue) SoftRed else MaterialTheme.colorScheme.primary
                    )
                    if (!vaccine.isCompleted) {
                        Button(onClick = { viewModel.markCompleted(vaccine) }) {
                            Text("Mark completed")
                        }
                    }
                }
            }
        }
    }
}
