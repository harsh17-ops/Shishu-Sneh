package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.DashboardRepository
import com.shishusneh.app.repository.DashboardSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class DashboardUiState(
    val loading: Boolean = true,
    val snapshot: DashboardSnapshot? = null,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    authRepository: AuthRepository,
    dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = DashboardUiState(loading = false)
                } else {
                    dashboardRepository.observeDashboard(userId).collect { snapshot ->
                        _uiState.value = DashboardUiState(loading = false, snapshot = snapshot)
                    }
                }
            }
        }
    }
}
