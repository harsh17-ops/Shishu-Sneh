package com.shishusneh.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shishusneh.app.ui.theme.OrangeAccent
import com.shishusneh.app.ui.theme.TealPrimary

@Composable
fun HeroCard(
    title: String,
    subtitle: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.linearGradient(listOf(TealPrimary, OrangeAccent)))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text(text = value, style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Text(text = subtitle, style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.88f))
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun DualMetricRow(leftTitle: String, leftValue: String, rightTitle: String, rightValue: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(title = leftTitle, value = leftValue, modifier = Modifier.weight(1f))
        MetricCard(title = rightTitle, value = rightValue, modifier = Modifier.weight(1f))
    }
}
