package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishusneh.app.data.entity.WeightEntryEntity
import com.shishusneh.app.repository.AuthRepository
import com.shishusneh.app.repository.BabyRepository
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.utils.SeedData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class GrowthUiState(
    val loading: Boolean = true,
    val babyId: Long? = null,
    val babyName: String = "",
    val dobMillis: Long = System.currentTimeMillis(),
    val entries: List<WeightEntryEntity> = emptyList(),
    val whoReference: List<Pair<Float, Float>> = SeedData.whoReferenceWeights(),
    val error: String? = null,
    val saveSuccessAt: Long? = null
)

@HiltViewModel
class GrowthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val babyRepository: BabyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GrowthUiState())
    val uiState: StateFlow<GrowthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserId.collectLatest { userId ->
                if (userId == null) {
                    _uiState.value = GrowthUiState(loading = false)
                } else {
                    babyRepository.observeProfile(userId).collectLatest { profile ->
                        if (profile == null) {
                            _uiState.value = GrowthUiState(loading = false)
                        } else {
                            babyRepository.observeWeights(profile.id).collect { entries ->
                                _uiState.value = GrowthUiState(
                                    loading = false,
                                    babyId = profile.id,
                                    babyName = profile.name,
                                    dobMillis = profile.dobMillis,
                                    entries = entries,
                                    saveSuccessAt = _uiState.value.saveSuccessAt
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun saveEntry(weight: String, height: String) {
        viewModelScope.launch {
            val babyId = _uiState.value.babyId ?: return@launch
            babyRepository.addGrowthEntry(babyId, weight, height)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(error = null, saveSuccessAt = System.currentTimeMillis())
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message)
                }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(error = null, saveSuccessAt = null)
    }
}
