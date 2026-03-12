package com.example.dscrcalculator.domain.util

import com.example.dscrcalculator.domain.model.DSCRStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class DSCRCalculatorTest {

    @Test
    fun `calculateDSCR returns expected ratio for valid inputs`() {
        val ratio = DSCRCalculator.calculateDSCR(
            rentalIncome = 5000.0,
            mortgage = 3000.0,
            tax = 500.0,
            insurance = 300.0,
            hoa = 200.0
        )

        assertEquals(1.25, ratio, 0.0001)
    }

    @Test
    fun `calculateDSCR returns zero when total expenses are zero`() {
        val ratio = DSCRCalculator.calculateDSCR(
            rentalIncome = 5000.0,
            mortgage = 0.0,
            tax = 0.0,
            insurance = 0.0,
            hoa = 0.0
        )

        assertEquals(0.0, ratio, 0.0)
    }

    @Test
    fun `getStatus returns strong when ratio is at strong threshold`() {
        val status = DSCRCalculator.getStatus(1.25)
        assertEquals(DSCRStatus.STRONG, status)
    }

    @Test
    fun `getStatus returns marginal when ratio is between 1_0 and 1_24`() {
        assertEquals(DSCRStatus.MARGINAL, DSCRCalculator.getStatus(1.0))
        assertEquals(DSCRStatus.MARGINAL, DSCRCalculator.getStatus(1.24))
    }

    @Test
    fun `getStatus returns rejected when ratio is below 1_0`() {
        val status = DSCRCalculator.getStatus(0.99)
        assertEquals(DSCRStatus.REJECTED, status)
    }

    @Test
    fun `calculateDSCR with zero income returns zero`() {
        val ratio = DSCRCalculator.calculateDSCR(0.0, 1000.0, 200.0, 100.0, 50.0)
        assertEquals(0.0, ratio, 0.0001)
    }

    @Test
    fun `calculateDSCR with only mortgage expense`() {
        val ratio = DSCRCalculator.calculateDSCR(2000.0, 1600.0, 0.0, 0.0, 0.0)
        assertEquals(1.25, ratio, 0.0001)
    }

    @Test
    fun `getStatus returns rejected for zero ratio`() {
        assertEquals(DSCRStatus.REJECTED, DSCRCalculator.getStatus(0.0))
    }
}
