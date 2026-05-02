package com.shishusneh.app.ui.screens.feeding

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
import com.shishusneh.app.ui.components.EmptyStateView
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.viewmodel.FeedingGuideViewModel

@Composable
fun FeedingGuideScreen(
    paddingValues: PaddingValues,
    viewModel: FeedingGuideViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.loading) {
        LoadingView()
        return
    }

    val profile = state.profile
    if (profile == null) {
        EmptyStateView(title = "Feeding guide unavailable", subtitle = "Create the baby profile first.")
        return
    }

    val ageMonths = DateUtils.ageInMonths(profile.dobMillis)
    val language = LocalConfiguration.current.locales[0]?.language ?: "en"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Age based guidance", style = MaterialTheme.typography.headlineMedium)
            Text(text = "For ${profile.name} - $ageMonths months")
            if (ageMonths < 6) {
                Text(
                    text = "Breastfeeding reminder: continue exclusive breastfeeding and feed on demand.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        items(state.tips, key = { it.id }) { tip ->
            val title = if (language == "hi") tip.titleHi else tip.titleEn
            val content = if (language == "hi") tip.contentHi else tip.contentEn
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                    Text(text = content)
                    Text(
                        text = if (tip.category == "myth") "Myth vs Fact" else "Daily tip",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
