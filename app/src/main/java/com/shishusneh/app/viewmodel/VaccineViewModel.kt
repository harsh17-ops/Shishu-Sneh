package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.VaccinationEntity
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.BabyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class VaccineUiState(
    val loading: Boolean = true,
    val vaccines: List<VaccinationEntity> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class VaccineViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccineUiState())
    val uiState: StateFlow<VaccineUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = VaccineUiState(loading = false)
                } else {
                    babyRepository.observeProfile(userId).collectLatest { profile ->
                        if (profile == null) {
                            _uiState.value = VaccineUiState(loading = false)
                        } else {
                            babyRepository.observeVaccines(profile.id).collect { vaccines ->
                                _uiState.value = VaccineUiState(loading = false, vaccines = vaccines)
                            }
                        }
                    }
                }
            }
        }
    }

    fun markCompleted(vaccination: VaccinationEntity) {
        viewModelScope.launch {
            babyRepository.markVaccinationComplete(vaccination)
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
