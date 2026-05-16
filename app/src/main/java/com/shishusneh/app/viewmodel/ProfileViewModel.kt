package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.BabyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ProfileUiState(
    val loading: Boolean = true,
    val id: Long? = null,
    val name: String = "",
    val dobMillis: Long = System.currentTimeMillis(),
    val gender: String = "Female",
    val bloodGroup: String = "",
    val motherName: String = "",
    val saving: Boolean = false,
    val error: String? = null,
    val savedAt: Long? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val userId = authRepository.currentUserId.firstOrNull() ?: return@launch
            babyRepository.observeProfile(userId).collect { profile ->
                _uiState.value = ProfileUiState(
                    loading = false,
                    id = profile?.id,
                    name = profile?.name.orEmpty(),
                    dobMillis = profile?.dobMillis ?: System.currentTimeMillis(),
                    gender = profile?.gender ?: "Female",
                    bloodGroup = profile?.bloodGroup.orEmpty(),
                    motherName = profile?.motherName.orEmpty(),
                    savedAt = _uiState.value.savedAt
                )
            }
        }
    }

    fun updateName(value: String) { _uiState.value = _uiState.value.copy(name = value) }
    fun updateDob(value: Long) { _uiState.value = _uiState.value.copy(dobMillis = value) }
    fun updateGender(value: String) { _uiState.value = _uiState.value.copy(gender = value) }
    fun updateBloodGroup(value: String) { _uiState.value = _uiState.value.copy(bloodGroup = value) }
    fun updateMotherName(value: String) { _uiState.value = _uiState.value.copy(motherName = value) }

    fun saveProfile() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId.firstOrNull() ?: return@launch
            _uiState.value = _uiState.value.copy(saving = true, error = null)
            babyRepository.saveProfile(
                userId = userId,
                existingId = _uiState.value.id,
                name = _uiState.value.name,
                dobMillis = _uiState.value.dobMillis,
                gender = _uiState.value.gender,
                bloodGroup = _uiState.value.bloodGroup,
                motherName = _uiState.value.motherName
            ).onSuccess {
                _uiState.value = _uiState.value.copy(saving = false, savedAt = System.currentTimeMillis())
            }.onFailure {
                _uiState.value = _uiState.value.copy(saving = false, error = it.message)
            }
        }
    }

    fun clearSavedFlag() {
        _uiState.value = _uiState.value.copy(savedAt = null)
    }
}
