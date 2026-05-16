package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.FamilyMemberEntity
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

data class FamilyUiState(
    val loading: Boolean = true,
    val babyId: Long? = null,
    val members: List<FamilyMemberEntity> = emptyList(),
    val error: String? = null,
    val savedAt: Long? = null
)

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository,
    private val advancedCareRepository: AdvancedCareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FamilyUiState())
    val uiState: StateFlow<FamilyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = FamilyUiState(loading = false)
                } else {
                    babyRepository.observeProfile(userId).collectLatest { profile ->
                        if (profile == null) {
                            _uiState.value = FamilyUiState(loading = false)
                        } else {
                            advancedCareRepository.observeFamilyMembers(profile.id).collect { members ->
                                _uiState.value = _uiState.value.copy(
                                    loading = false,
                                    babyId = profile.id,
                                    members = members
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun addMember(name: String, relation: String, phone: String, accessLevel: String) {
        viewModelScope.launch {
            val babyId = _uiState.value.babyId ?: return@launch
            advancedCareRepository.addFamilyMember(babyId, name, relation, phone, accessLevel)
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
