package com.shishusneh.app.ui.screens.growth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.EmptyStateView
import com.shishusneh.app.ui.components.GrowthChartView
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.viewmodel.GrowthViewModel

@Composable
fun GrowthScreen(
    paddingValues: PaddingValues,
    viewModel: GrowthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    LaunchedEffect(state.saveSuccessAt) {
        if (state.saveSuccessAt != null) {
            showDialog = false
            weight = ""
            height = ""
            viewModel.clearMessage()
        }
    }

    if (state.loading) {
        LoadingView()
        return
    }

    if (state.babyId == null) {
        EmptyStateView(title = "Growth tracker unavailable", subtitle = "Create the baby profile first.")
        return
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(text = "Growth chart", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Compare your baby's recorded weight with a simple WHO reference line.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            item {
                GrowthChartView(
                    dobMillis = state.dobMillis,
                    entries = state.entries,
                    referenceLine = state.whoReference
                )
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = state.percentileLabel, style = MaterialTheme.typography.titleLarge)
                    Text(text = state.percentileInterpretation, style = MaterialTheme.typography.bodyLarge)
                }
            }
            if (state.entries.isEmpty()) {
                item {
                    EmptyStateView(
                        title = "No growth records yet",
                        subtitle = "Tap the + button to add weight and height."
                    )
                }
            } else {
                items(state.entries.reversed(), key = { it.id }) { entry ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${entry.weightKg} kg - ${entry.heightCm} cm",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = DateUtils.formatDate(entry.recordedAt),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add growth entry") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") }
                    )
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)") }
                    )
                    state.error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.saveEntry(weight, height) }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}
