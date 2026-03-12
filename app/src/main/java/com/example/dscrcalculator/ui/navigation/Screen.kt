package com.example.dscrcalculator.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dscrcalculator.R

sealed class Screen(val route: String, val titleResId: Int, val icon: ImageVector) {
    object Calculator : Screen("calculator", R.string.calculator, Icons.Default.Calculate)
    object History : Screen("history", R.string.history, Icons.Default.History)

    companion object {
        const val HISTORY_DETAIL_ARG = "calculationId"
        const val HISTORY_DETAIL_ROUTE = "history_detail/{$HISTORY_DETAIL_ARG}"

        fun historyDetailRoute(calculationId: Int): String {
            return "history_detail/$calculationId"
        }
    }
}
