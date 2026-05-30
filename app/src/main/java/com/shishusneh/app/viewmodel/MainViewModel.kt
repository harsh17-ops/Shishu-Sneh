package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.repository.AdvancedCareRepository
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
import kotlinx.coroutines.launch

data class MainUiState(
    val loading: Boolean = true,
    val currentUserId: Long? = null,
    val hasProfile: Boolean = false,
    val settings: AppSettings = AppSettings()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository,
    private val advancedCareRepository: AdvancedCareRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            babyRepository.ensureReferenceData()
            advancedCareRepository.ensureAdvancedSeedData()
            settingsRepository.settings.collect { settings ->
                _uiState.value = _uiState.value.copy(settings = settings)
            }
        }
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        currentUserId = null,
                        hasProfile = false
                    )
                } else {
                    babyRepository.observeProfile(userId).collect { profile ->
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            currentUserId = userId,
                            hasProfile = profile != null
                        )
                    }
                }
            }
        }
    }
}
