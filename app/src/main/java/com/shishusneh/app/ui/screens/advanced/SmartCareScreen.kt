package com.shishusneh.app.ui.screens.advanced

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.viewmodel.SmartCareViewModel
import java.util.Locale

@Composable
fun SmartCareScreen(
    paddingValues: PaddingValues,
    viewModel: SmartCareViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var question by remember { mutableStateOf("") }
    val tts = remember {
        TextToSpeech(context) { }
    }

    DisposableEffect(Unit) {
        onDispose { tts.stop(); tts.shutdown() }
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) viewModel.scanVaccinationCard(uri)
    }
    val speechLauncher = rememberLauncherForActivityResult(
        contract = StartActivityForResult()
    ) { result ->
        val spoken = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            .orEmpty()
        if (spoken.isNotBlank()) {
            question = spoken
            viewModel.askQuestion(spoken)
        }
    }
    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        val file = state.pdfFile
        if (uri != null && file != null) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                file.inputStream().use { input -> input.copyTo(output) }
            }
            Toast.makeText(context, "PDF report saved", Toast.LENGTH_SHORT).show()
        }
    }

    if (state.loading) {
        LoadingView()
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Smart Care Hub", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("AI Weekly Summary", style = MaterialTheme.typography.titleLarge)
                    Text(state.weeklySummary?.content ?: "Summary is being prepared.")
                    Button(onClick = {
                        tts.language = Locale.getDefault()
                        tts.speak(state.weeklySummary?.content ?: "", TextToSpeech.QUEUE_FLUSH, null, "summary")
                    }) {
                        Text("Play Voice Summary")
                    }
                    Button(onClick = { viewModel.exportPdf() }) {
                        Text("Generate PDF Report")
                    }
                    if (state.pdfFile != null) {
                        Button(onClick = { pdfLauncher.launch("shishu_sneh_report.pdf") }) {
                            Text("Save Generated PDF")
                        }
                    }
                }
            }
        }
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("AI Baby Care Chat", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Ask a baby care question") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = {
                        viewModel.askQuestion(question)
                        question = ""
                    }) {
                        Text("Ask Assistant")
                    }
                    Button(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your question")
                        }
                        speechLauncher.launch(intent)
                    }) {
                        Text("Speak in Hindi/English")
                    }
                    state.chat.takeLast(6).forEach { message ->
                        Text(
                            text = if (message.fromUser) "You: ${message.text}" else "Assistant: ${message.text}"
                        )
                    }
                }
            }
        }
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("OCR Vaccination Card Scan", style = MaterialTheme.typography.titleLarge)
                    Button(onClick = { imageLauncher.launch("image/*") }) {
                        Text("Pick Vaccination Card Image")
                    }
                    if (state.lastOcrText.isNotBlank()) {
                        Text(state.lastOcrText)
                    }
                }
            }
        }
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Cloud Backup", style = MaterialTheme.typography.titleLarge)
                    Text(state.cloudStatus)
                }
            }
        }
        if (state.scans.isNotEmpty()) {
            item {
                Text("Recent card scans", style = MaterialTheme.typography.titleLarge)
            }
            items(state.scans.take(3), key = { it.id }) { scan ->
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(scan.sourceLabel, style = MaterialTheme.typography.titleLarge)
                        Text(scan.extractedText.take(180))
                    }
                }
            }
        }
        state.error?.let { message ->
            item { Text(message, color = MaterialTheme.colorScheme.error) }
        }
    }
}
