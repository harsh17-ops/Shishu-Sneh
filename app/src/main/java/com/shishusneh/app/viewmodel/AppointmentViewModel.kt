package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.AppointmentEntity
import com.shishusneh.app.repository.AdvancedCareRepository
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

data class AppointmentUiState(
    val loading: Boolean = true,
    val babyId: Long? = null,
    val appointments: List<AppointmentEntity> = emptyList(),
    val error: String? = null,
    val savedAt: Long? = null
)

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository,
    private val advancedCareRepository: AdvancedCareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentUiState())
    val uiState: StateFlow<AppointmentUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = AppointmentUiState(loading = false)
                } else {
                    babyRepository.observeProfile(userId).collectLatest { profile ->
                        if (profile == null) {
                            _uiState.value = AppointmentUiState(loading = false)
                        } else {
                            advancedCareRepository.observeAppointments(profile.id).collect { appointments ->
                                _uiState.value = _uiState.value.copy(
                                    loading = false,
                                    babyId = profile.id,
                                    appointments = appointments
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun addAppointment(title: String, doctorName: String, appointmentAtMillis: Long, notes: String) {
        viewModelScope.launch {
            val babyId = _uiState.value.babyId ?: return@launch
            advancedCareRepository.addAppointment(babyId, title, doctorName, appointmentAtMillis, notes)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(error = null, savedAt = System.currentTimeMillis())
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message)
                }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(error = null, savedAt = null)
    }
}
