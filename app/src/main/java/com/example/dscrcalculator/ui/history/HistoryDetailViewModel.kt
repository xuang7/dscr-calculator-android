package com.example.dscrcalculator.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dscrcalculator.data.local.CalculationEntity
import com.example.dscrcalculator.data.repository.CalculationRepository
import com.example.dscrcalculator.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val repository: CalculationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryDetailUiState())
    val uiState: StateFlow<HistoryDetailUiState> = _uiState.asStateFlow()

    private val calculationId: Int = checkNotNull(savedStateHandle[Screen.HISTORY_DETAIL_ARG])

    init {
        loadCalculation()
    }

    private fun loadCalculation() {
        viewModelScope.launch {
            val calculation = repository.getCalculationById(calculationId)
            _uiState.value = HistoryDetailUiState(
                isLoading = false,
                calculation = calculation
            )
        }
    }
}

data class HistoryDetailUiState(
    val isLoading: Boolean = true,
    val calculation: CalculationEntity? = null
)
