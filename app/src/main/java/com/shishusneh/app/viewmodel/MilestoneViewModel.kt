package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.MilestoneEntity
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.BabyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class MilestoneUiState(
    val loading: Boolean = true,
    val milestones: List<MilestoneEntity> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MilestoneUiState())
    val uiState: StateFlow<MilestoneUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = MilestoneUiState(loading = false)
                } else {
                    babyRepository.observeProfile(userId).collectLatest { profile ->
                        if (profile == null) {
                            _uiState.value = MilestoneUiState(loading = false)
                        } else {
                            babyRepository.observeMilestones(profile.id).collect { milestones ->
                                _uiState.value = MilestoneUiState(loading = false, milestones = milestones)
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateMilestone(milestone: MilestoneEntity, achieved: Boolean) {
        viewModelScope.launch {
            babyRepository.updateMilestone(milestone, achieved)
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
