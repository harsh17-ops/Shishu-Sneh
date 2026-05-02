package com.shishusneh.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shishusneh.app.utils.DateUtils

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectorField(
    valueMillis: Long,
    onValueChange: (Long) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState(initialSelectedDateMillis = valueMillis)

    OutlinedTextField(
        value = DateUtils.formatDate(valueMillis),
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable { openDialog.value = true }
    )

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let(onValueChange)
                    openDialog.value = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = dateState,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
