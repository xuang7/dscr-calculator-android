package com.example.dscrcalculator.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dscrcalculator.data.local.CalculationEntity
import com.example.dscrcalculator.data.repository.CalculationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: CalculationRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = repository.getAllCalculations()
        .map { HistoryUiState(calculations = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState()
        )

    fun deleteCalculation(calculation: CalculationEntity) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
        }
    }
}

data class HistoryUiState(
    val calculations: List<CalculationEntity> = emptyList()
)
