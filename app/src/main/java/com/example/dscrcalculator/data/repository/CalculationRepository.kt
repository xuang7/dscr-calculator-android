package com.example.dscrcalculator.data.repository

import com.example.dscrcalculator.data.local.CalculationDao
import com.example.dscrcalculator.data.local.CalculationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationRepository @Inject constructor(
    private val calculationDao: CalculationDao
) {
    fun getAllCalculations(): Flow<List<CalculationEntity>> = calculationDao.getAllCalculations()

    suspend fun insertCalculation(calculation: CalculationEntity) =
        calculationDao.insertCalculation(calculation)

    suspend fun deleteCalculation(calculation: CalculationEntity) =
        calculationDao.deleteCalculation(calculation)

    suspend fun getCalculationById(id: Int): CalculationEntity? =
        calculationDao.getCalculationById(id)
}
