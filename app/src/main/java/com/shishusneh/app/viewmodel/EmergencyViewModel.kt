package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.EmergencyGuideEntity
import com.shishusneh.app.repository.AdvancedCareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class EmergencyUiState(
    val loading: Boolean = true,
    val guides: List<EmergencyGuideEntity> = emptyList()
)

@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val advancedCareRepository: AdvancedCareRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EmergencyUiState())
    val uiState: StateFlow<EmergencyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            advancedCareRepository.observeEmergencyGuides().collect { guides ->
                _uiState.value = EmergencyUiState(loading = false, guides = guides)
            }
        }
    }
}
