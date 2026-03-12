package com.example.dscrcalculator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val propertyAddress: String,
    val monthlyRentalIncome: Double,
    val monthlyMortgagePayment: Double,
    val monthlyPropertyTax: Double,
    val monthlyInsurance: Double,
    val monthlyHoaFee: Double,
    val dscrRatio: Double,
    val timestamp: Long = System.currentTimeMillis()
)
