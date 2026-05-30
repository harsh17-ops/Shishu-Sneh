package com.shishusneh.app.ui.screens.advanced

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.viewmodel.EmergencyViewModel

@Composable
fun EmergencyGuidanceScreen(
    paddingValues: PaddingValues,
    viewModel: EmergencyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val language = LocalConfiguration.current.locales[0]?.language ?: "en"
    if (state.loading) {
        LoadingView()
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Emergency Guidance", style = MaterialTheme.typography.headlineMedium) }
        items(state.guides, key = { it.id }) { guide ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        if (language == "hi") guide.titleHi else guide.titleEn,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(if (language == "hi") guide.contentHi else guide.contentEn)
                }
            }
        }
    }
}
