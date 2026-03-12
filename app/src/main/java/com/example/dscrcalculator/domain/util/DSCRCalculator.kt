package com.example.dscrcalculator.domain.util

import com.example.dscrcalculator.domain.model.DSCRStatus

object DSCRCalculator {
    const val STRONG_THRESHOLD = 1.25
    const val MARGINAL_THRESHOLD = 1.0

    fun calculateDSCR(
        rentalIncome: Double,
        mortgage: Double,
        tax: Double,
        insurance: Double,
        hoa: Double
    ): Double {
        val totalExpenses = mortgage + tax + insurance + hoa
        if (totalExpenses == 0.0) return 0.0
        return rentalIncome / totalExpenses
    }

    fun getStatus(dscr: Double): DSCRStatus {
        return when {
            dscr >= STRONG_THRESHOLD -> DSCRStatus.STRONG
            dscr >= MARGINAL_THRESHOLD -> DSCRStatus.MARGINAL
            else -> DSCRStatus.REJECTED
        }
    }
}
