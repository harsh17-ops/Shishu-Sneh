package com.shishusneh.app.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.shishusneh.app.ui.components.AppTextField
import com.shishusneh.app.ui.components.DateSelectorField
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditorScreen(
    onSaved: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Male", "Female", "Other")

    LaunchedEffect(state.savedAt) {
        if (state.savedAt != null) {
            onSaved()
            viewModel.clearSavedFlag()
        }
    }

    if (state.loading) {
        LoadingView()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Baby profile",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Create a trusted health record for your baby's first year.",
            style = MaterialTheme.typography.bodyLarge
        )
        AppTextField(value = state.name, onValueChange = viewModel::updateName, label = "Baby name")
        DateSelectorField(valueMillis = state.dobMillis, onValueChange = viewModel::updateDob, label = "Date of birth")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = state.gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            viewModel.updateGender(gender)
                            expanded = false
                        }
                    )
                }
            }
        }

        AppTextField(value = state.bloodGroup, onValueChange = viewModel::updateBloodGroup, label = "Blood group")
        AppTextField(value = state.motherName, onValueChange = viewModel::updateMotherName, label = "Mother name")
        state.error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = viewModel::saveProfile,
            enabled = !state.saving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.saving) "Saving..." else "Save profile")
        }
    }
}
