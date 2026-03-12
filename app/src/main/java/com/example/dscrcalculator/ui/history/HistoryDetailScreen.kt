package com.example.dscrcalculator.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dscrcalculator.R
import com.example.dscrcalculator.data.local.CalculationEntity
import com.example.dscrcalculator.domain.util.DSCRCalculator
import com.example.dscrcalculator.ui.common.AppBarMenus
import com.example.dscrcalculator.ui.common.statusColor
import com.example.dscrcalculator.ui.common.statusTextRes
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.calculation_details)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = { AppBarMenus() }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.calculation == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.calculation_not_found))
                }
            }

            else -> {
                uiState.calculation?.let { calculation ->
                    HistoryDetailContent(
                        calculation = calculation,
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryDetailContent(
    calculation: CalculationEntity,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val status = DSCRCalculator.getStatus(calculation.dscrRatio)
    val statusColor = status.statusColor()
    val totalExpenses = calculation.monthlyMortgagePayment +
        calculation.monthlyPropertyTax +
        calculation.monthlyInsurance +
        calculation.monthlyHoaFee

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.dscr_ratio, calculation.dscrRatio),
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(status.statusTextRes()),
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.saved_on,
                        dateFormat.format(Date(calculation.timestamp))
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                DetailRow(
                    label = stringResource(R.string.property_address),
                    value = calculation.propertyAddress
                )
                DetailRow(
                    label = stringResource(R.string.monthly_rental_income),
                    value = currencyFormatter.format(calculation.monthlyRentalIncome)
                )
                DetailRow(
                    label = stringResource(R.string.monthly_mortgage),
                    value = currencyFormatter.format(calculation.monthlyMortgagePayment)
                )
                DetailRow(
                    label = stringResource(R.string.monthly_tax),
                    value = currencyFormatter.format(calculation.monthlyPropertyTax)
                )
                DetailRow(
                    label = stringResource(R.string.monthly_insurance),
                    value = currencyFormatter.format(calculation.monthlyInsurance)
                )
                DetailRow(
                    label = stringResource(R.string.monthly_hoa),
                    value = currencyFormatter.format(calculation.monthlyHoaFee)
                )
                DetailRow(
                    label = stringResource(R.string.monthly_total_expenses),
                    value = currencyFormatter.format(totalExpenses),
                    emphasize = true
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    emphasize: Boolean = false
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (emphasize) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
