package com.example.dscrcalculator.ui.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dscrcalculator.R
import com.example.dscrcalculator.domain.model.DSCRStatus
import com.example.dscrcalculator.ui.common.LanguageMenu
import com.example.dscrcalculator.ui.common.statusColor
import com.example.dscrcalculator.ui.common.statusTextRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.calculator)) },
                actions = { LanguageMenu() }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DSCRResultCard(uiState.dscrRatio, uiState.dscrStatus)
            }

            item {
                Text(
                    text = stringResource(R.string.input_range_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.propertyAddress,
                    onValueChange = viewModel::onPropertyAddressChange,
                    label = { Text(stringResource(R.string.property_address)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                CurrencyTextField(
                    value = uiState.monthlyRentalIncome,
                    onValueChange = viewModel::onRentalIncomeChange,
                    label = stringResource(R.string.monthly_rental_income)
                )
            }

            item {
                CurrencyTextField(
                    value = uiState.monthlyMortgagePayment,
                    onValueChange = viewModel::onMortgageChange,
                    label = stringResource(R.string.monthly_mortgage)
                )
            }

            item {
                CurrencyTextField(
                    value = uiState.monthlyPropertyTax,
                    onValueChange = viewModel::onTaxChange,
                    label = stringResource(R.string.monthly_tax)
                )
            }

            item {
                CurrencyTextField(
                    value = uiState.monthlyInsurance,
                    onValueChange = viewModel::onInsuranceChange,
                    label = stringResource(R.string.monthly_insurance)
                )
            }

            item {
                CurrencyTextField(
                    value = uiState.monthlyHoaFee,
                    onValueChange = viewModel::onHoaFeeChange,
                    label = stringResource(R.string.monthly_hoa)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = viewModel::saveCalculation,
                        modifier = Modifier.weight(1f),
                        enabled = uiState.isValid && uiState.propertyAddress.isNotBlank() && !uiState.isSaved
                    ) {
                        Text(stringResource(R.string.save))
                    }

                    OutlinedButton(
                        onClick = viewModel::reset,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.reset))
                    }
                }
            }
        }
    }
}

@Composable
fun DSCRResultCard(ratio: Double, status: DSCRStatus) {
    val statusColor = status.statusColor()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.dscr_ratio, ratio),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(status.statusTextRes()),
                color = statusColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        prefix = { Text("$ ") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true
    )
}
