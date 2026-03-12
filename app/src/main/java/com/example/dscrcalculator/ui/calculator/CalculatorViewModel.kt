package com.example.dscrcalculator.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dscrcalculator.data.local.CalculationEntity
import com.example.dscrcalculator.data.repository.CalculationRepository
import com.example.dscrcalculator.domain.model.DSCRStatus
import com.example.dscrcalculator.domain.util.DSCRCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CalculationRepository
) : ViewModel() {

    companion object {
        private const val MAX_MONTHLY_AMOUNT = 1_000_000.0
        private const val MAX_ADDRESS_LENGTH = 120
    }

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onPropertyAddressChange(value: String) {
        if (value.length <= MAX_ADDRESS_LENGTH) {
            _uiState.update { it.copy(propertyAddress = value, isSaved = false) }
        }
    }

    fun onRentalIncomeChange(value: String) {
        if (isValidInput(value)) {
            _uiState.update { it.copy(monthlyRentalIncome = value, isSaved = false) }
            calculate()
        }
    }

    fun onMortgageChange(value: String) {
        if (isValidInput(value)) {
            _uiState.update { it.copy(monthlyMortgagePayment = value, isSaved = false) }
            calculate()
        }
    }

    fun onTaxChange(value: String) {
        if (isValidInput(value)) {
            _uiState.update { it.copy(monthlyPropertyTax = value, isSaved = false) }
            calculate()
        }
    }

    fun onInsuranceChange(value: String) {
        if (isValidInput(value)) {
            _uiState.update { it.copy(monthlyInsurance = value, isSaved = false) }
            calculate()
        }
    }

    fun onHoaFeeChange(value: String) {
        if (isValidInput(value)) {
            _uiState.update { it.copy(monthlyHoaFee = value, isSaved = false) }
            calculate()
        }
    }

    private fun isValidInput(value: String): Boolean {
        if (value.isEmpty()) return true
        val numericValue = value.toDoubleOrNull() ?: return false
        return numericValue in 0.0..MAX_MONTHLY_AMOUNT
    }

    private fun calculate() {
        val state = _uiState.value
        val rental = state.monthlyRentalIncome.toDoubleOrNull() ?: 0.0
        val mortgage = state.monthlyMortgagePayment.toDoubleOrNull() ?: 0.0
        val tax = state.monthlyPropertyTax.toDoubleOrNull() ?: 0.0
        val insurance = state.monthlyInsurance.toDoubleOrNull() ?: 0.0
        val hoa = state.monthlyHoaFee.toDoubleOrNull() ?: 0.0

        val ratio = DSCRCalculator.calculateDSCR(rental, mortgage, tax, insurance, hoa)
        val status = DSCRCalculator.getStatus(ratio)

        _uiState.update {
            it.copy(
                dscrRatio = ratio,
                dscrStatus = status,
                isValid = rental > 0 && (mortgage + tax + insurance + hoa) > 0
            )
        }
    }

    fun saveCalculation() {
        val state = _uiState.value
        if (state.propertyAddress.isBlank() || !state.isValid || state.isSaved) return
        _uiState.update { it.copy(isSaved = true) }

        viewModelScope.launch {
            val entity = CalculationEntity(
                propertyAddress = state.propertyAddress,
                monthlyRentalIncome = state.monthlyRentalIncome.toDoubleOrNull() ?: 0.0,
                monthlyMortgagePayment = state.monthlyMortgagePayment.toDoubleOrNull() ?: 0.0,
                monthlyPropertyTax = state.monthlyPropertyTax.toDoubleOrNull() ?: 0.0,
                monthlyInsurance = state.monthlyInsurance.toDoubleOrNull() ?: 0.0,
                monthlyHoaFee = state.monthlyHoaFee.toDoubleOrNull() ?: 0.0,
                dscrRatio = state.dscrRatio
            )
            runCatching { repository.insertCalculation(entity) }
                .onFailure {
                    _uiState.update { currentState -> currentState.copy(isSaved = false) }
                }
        }
    }

    fun reset() {
        _uiState.value = CalculatorUiState()
    }
}

data class CalculatorUiState(
    val propertyAddress: String = "",
    val monthlyRentalIncome: String = "",
    val monthlyMortgagePayment: String = "",
    val monthlyPropertyTax: String = "",
    val monthlyInsurance: String = "",
    val monthlyHoaFee: String = "",
    val dscrRatio: Double = 0.0,
    val dscrStatus: DSCRStatus = DSCRStatus.REJECTED,
    val isValid: Boolean = false,
    val isSaved: Boolean = false
)
