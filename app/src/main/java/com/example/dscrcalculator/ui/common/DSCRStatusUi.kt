package com.example.dscrcalculator.ui.common

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.dscrcalculator.R
import com.example.dscrcalculator.domain.model.DSCRStatus

private val StrongColor = Color(0xFF4CAF50)
private val MarginalColor = Color(0xFFFFC107)
private val RejectedColor = Color(0xFFF44336)

fun DSCRStatus.statusColor(): Color {
    return when (this) {
        DSCRStatus.STRONG -> StrongColor
        DSCRStatus.MARGINAL -> MarginalColor
        DSCRStatus.REJECTED -> RejectedColor
    }
}

@StringRes
fun DSCRStatus.statusTextRes(): Int {
    return when (this) {
        DSCRStatus.STRONG -> R.string.strong
        DSCRStatus.MARGINAL -> R.string.marginal
        DSCRStatus.REJECTED -> R.string.does_not_qualify
    }
}
