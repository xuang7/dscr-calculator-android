package com.example.dscrcalculator.ui.calculator

import com.example.dscrcalculator.data.local.CalculationDao
import com.example.dscrcalculator.data.local.CalculationEntity
import com.example.dscrcalculator.data.repository.CalculationRepository
import com.example.dscrcalculator.domain.model.DSCRStatus
import com.example.dscrcalculator.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalculatorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeDao: FakeCalculationDao
    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        fakeDao = FakeCalculationDao()
        viewModel = CalculatorViewModel(CalculationRepository(fakeDao))
    }

    @Test
    fun `on amount changes updates ratio status and validity`() {
        viewModel.onRentalIncomeChange("5000")
        viewModel.onMortgageChange("3000")
        viewModel.onTaxChange("500")
        viewModel.onInsuranceChange("300")
        viewModel.onHoaFeeChange("200")

        val state = viewModel.uiState.value
        assertTrue(state.isValid)
        assertEquals(1.25, state.dscrRatio, 0.0001)
        assertEquals(DSCRStatus.STRONG, state.dscrStatus)
    }

    @Test
    fun `invalid amount input is ignored`() {
        viewModel.onRentalIncomeChange("2000")
        viewModel.onRentalIncomeChange("-1")
        viewModel.onRentalIncomeChange("1000001")

        assertEquals("2000", viewModel.uiState.value.monthlyRentalIncome)
    }

    @Test
    fun `saveCalculation inserts only once when user taps save repeatedly`() = runTest {
        fillValidInputs()
        viewModel.saveCalculation()
        viewModel.saveCalculation()
        advanceUntilIdle()

        assertEquals(1, fakeDao.insertCount)
        assertTrue(viewModel.uiState.value.isSaved)
    }

    @Test
    fun `input changes after save reset isSaved and allow saving again`() = runTest {
        fillValidInputs()
        viewModel.saveCalculation()
        advanceUntilIdle()

        viewModel.onTaxChange("600")
        assertFalse(viewModel.uiState.value.isSaved)

        viewModel.saveCalculation()
        advanceUntilIdle()

        assertEquals(2, fakeDao.insertCount)
    }

    @Test
    fun `saveCalculation does nothing when address is blank`() = runTest {
        viewModel.onRentalIncomeChange("5000")
        viewModel.onMortgageChange("3000")
        viewModel.onTaxChange("500")
        viewModel.onInsuranceChange("300")
        viewModel.onHoaFeeChange("200")

        viewModel.saveCalculation()
        advanceUntilIdle()

        assertEquals(0, fakeDao.insertCount)
    }

    private fun fillValidInputs() {
        viewModel.onPropertyAddressChange("123 Main St")
        viewModel.onRentalIncomeChange("5000")
        viewModel.onMortgageChange("3000")
        viewModel.onTaxChange("500")
        viewModel.onInsuranceChange("300")
        viewModel.onHoaFeeChange("200")
    }
}

private class FakeCalculationDao : CalculationDao {
    private val items = mutableListOf<CalculationEntity>()
    private val calculationsFlow = MutableStateFlow<List<CalculationEntity>>(emptyList())

    val insertCount: Int
        get() = items.size

    override fun getAllCalculations(): Flow<List<CalculationEntity>> = calculationsFlow

    override suspend fun insertCalculation(calculation: CalculationEntity) {
        val entityWithId = if (calculation.id == 0) {
            calculation.copy(id = items.size + 1)
        } else {
            calculation
        }
        items.add(entityWithId)
        calculationsFlow.value = items.sortedByDescending { it.timestamp }
    }

    override suspend fun deleteCalculation(calculation: CalculationEntity) {
        items.removeIf { it.id == calculation.id }
        calculationsFlow.value = items.sortedByDescending { it.timestamp }
    }

    override suspend fun getCalculationById(id: Int): CalculationEntity? {
        return items.firstOrNull { it.id == id }
    }
}
