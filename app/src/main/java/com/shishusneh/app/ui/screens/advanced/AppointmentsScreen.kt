package com.shishusneh.app.ui.screens.advanced

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.shishusneh.app.ui.components.DateSelectorField
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.viewmodel.AppointmentViewModel

@Composable
fun AppointmentsScreen(
    paddingValues: PaddingValues,
    viewModel: AppointmentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var doctor by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(state.savedAt) {
        if (state.savedAt != null) {
            showDialog = false
            title = ""
            doctor = ""
            notes = ""
            viewModel.clearMessage()
        }
    }

    if (state.loading) {
        LoadingView()
        return
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Text("+") }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Doctor Appointments", style = MaterialTheme.typography.headlineMedium) }
            items(state.appointments, key = { it.id }) { appointment ->
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(appointment.title, style = MaterialTheme.typography.titleLarge)
                        Text("Dr. ${appointment.doctorName}")
                        Text(DateUtils.formatDate(appointment.appointmentAtMillis))
                        if (appointment.notes.isNotBlank()) Text(appointment.notes)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Appointment") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                    OutlinedTextField(value = doctor, onValueChange = { doctor = it }, label = { Text("Doctor name") })
                    DateSelectorField(valueMillis = date, onValueChange = { date = it }, label = "Appointment date")
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") })
                    state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.addAppointment(title, doctor, date, notes) }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}
