package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(loading = true)
            authRepository.login(email, password)
                .onSuccess { _uiState.value = AuthUiState() }
                .onFailure { _uiState.value = AuthUiState(error = it.message) }
        }
    }

    fun signup(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(loading = true)
            authRepository.signup(fullName, email, password)
                .onSuccess { _uiState.value = AuthUiState() }
                .onFailure { _uiState.value = AuthUiState(error = it.message) }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
