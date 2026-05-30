package com.shishusneh.app.ui.screens.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    onEditProfile: () -> Unit,
    onOpenSmartCare: () -> Unit,
    onOpenAppointments: () -> Unit,
    onOpenFamilyAccess: () -> Unit,
    onOpenEmergency: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val exportPayload = remember { mutableStateOf("") }
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use {
                it.write(exportPayload.value.toByteArray())
            }
            Toast.makeText(context, "Backup exported", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Notifications", style = MaterialTheme.typography.titleLarge)
            Switch(
                checked = state.settings.notificationsEnabled,
                onCheckedChange = viewModel::setNotifications
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Language", style = MaterialTheme.typography.titleLarge)
            listOf("en" to "English", "hi" to "Hindi").forEach { (tag, label) ->
                RowOption(
                    selected = state.settings.languageTag == tag,
                    label = label,
                    onClick = { viewModel.setLanguage(tag) }
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Theme", style = MaterialTheme.typography.titleLarge)
            listOf("system" to "System", "light" to "Light", "dark" to "Dark").forEach { (mode, label) ->
                RowOption(
                    selected = state.settings.themeMode == mode,
                    label = label,
                    onClick = { viewModel.setThemeMode(mode) }
                )
            }
        }

        Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Edit baby profile")
        }

        Button(onClick = onOpenSmartCare, modifier = Modifier.fillMaxWidth()) {
            Text("Open Smart Care Hub")
        }

        Button(onClick = onOpenAppointments, modifier = Modifier.fillMaxWidth()) {
            Text("Doctor Appointments")
        }

        Button(onClick = onOpenFamilyAccess, modifier = Modifier.fillMaxWidth()) {
            Text("Family Access")
        }

        Button(onClick = onOpenEmergency, modifier = Modifier.fillMaxWidth()) {
            Text("Emergency Guidance")
        }

        Button(
            onClick = {
                scope.launch {
                    runCatching {
                        exportPayload.value = viewModel.exportData()
                        exportLauncher.launch("shishu_sneh_backup.json")
                    }.onFailure {
                        Toast.makeText(context, it.message ?: "Export failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Export backup")
        }

        Button(onClick = viewModel::logout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}

@Composable
private fun RowOption(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, modifier = Modifier.padding(top = 12.dp))
    }
}
