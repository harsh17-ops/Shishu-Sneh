package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.repository.AppSettings
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.BabyRepository
import com.shishusneh.app.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class SettingsUiState(
    val loading: Boolean = true,
    val settings: AppSettings = AppSettings(),
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.value = SettingsUiState(loading = false, settings = settings)
            }
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
            authRepository.currentUserId.firstOrNull()?.let { babyRepository.rescheduleAllVaccines(it, enabled) }
        }
    }

    fun setLanguage(languageTag: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(languageTag)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    suspend fun exportData(): String {
        val userId = authRepository.currentUserId.firstOrNull() ?: error("Please login first")
        return babyRepository.exportData(userId)
    }
}
