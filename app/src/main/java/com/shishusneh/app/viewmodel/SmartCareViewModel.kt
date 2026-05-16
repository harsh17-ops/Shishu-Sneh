package com.shishusneh.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.VaccineScanEntity
import com.shishusneh.app.repository.AdvancedCareRepository
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.BabyRepository
import com.shishusneh.app.repository.WeeklySummary
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ChatMessage(
    val fromUser: Boolean,
    val text: String
)

data class SmartCareUiState(
    val loading: Boolean = true,
    val babyId: Long? = null,
    val chat: List<ChatMessage> = emptyList(),
    val weeklySummary: WeeklySummary? = null,
    val cloudStatus: String = "",
    val scans: List<VaccineScanEntity> = emptyList(),
    val lastOcrText: String = "",
    val pdfFile: File? = null,
    val error: String? = null
)

@HiltViewModel
class SmartCareViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository,
    private val advancedCareRepository: AdvancedCareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmartCareUiState())
    val uiState: StateFlow<SmartCareUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = SmartCareUiState(loading = false)
                } else {
                    val summary = advancedCareRepository.generateWeeklySummary(userId)
                    babyRepository.observeProfile(userId).collectLatest { profile ->
                        if (profile == null) {
                            _uiState.value = SmartCareUiState(loading = false)
                        } else {
                            advancedCareRepository.observeScans(profile.id).collect { scans ->
                                _uiState.value = _uiState.value.copy(
                                    loading = false,
                                    babyId = profile.id,
                                    weeklySummary = summary,
                                    cloudStatus = advancedCareRepository.cloudSyncStatus(),
                                    scans = scans
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun askQuestion(question: String) {
        if (question.isBlank()) return
        val answer = advancedCareRepository.answerCareQuestion(question)
        _uiState.value = _uiState.value.copy(
            chat = _uiState.value.chat + ChatMessage(true, question.trim()) + ChatMessage(false, answer)
        )
    }

    fun scanVaccinationCard(uri: Uri) {
        viewModelScope.launch {
            val babyId = _uiState.value.babyId ?: return@launch
            advancedCareRepository.extractVaccineCardText(babyId, uri)
                .onSuccess { text ->
                    _uiState.value = _uiState.value.copy(lastOcrText = text, error = null)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message)
                }
        }
    }

    fun exportPdf() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId.firstOrNull() ?: return@launch
            runCatching { advancedCareRepository.exportPdfHealthReport(userId) }
                .onSuccess { file -> _uiState.value = _uiState.value.copy(pdfFile = file, error = null) }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
