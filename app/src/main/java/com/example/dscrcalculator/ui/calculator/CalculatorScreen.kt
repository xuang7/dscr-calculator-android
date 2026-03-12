package com.example.dscrcalculator.ui.calculator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dscrcalculator.R
import com.example.dscrcalculator.domain.model.DSCRStatus
import com.example.dscrcalculator.ui.common.AppBarMenus
import com.example.dscrcalculator.ui.common.statusColor
import com.example.dscrcalculator.ui.common.statusTextRes
import java.util.Locale

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
                actions = { AppBarMenus() }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DSCRResultCard(
                    ratio = uiState.dscrRatio,
                    status = uiState.dscrStatus,
                    isActive = uiState.isValid
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.propertyAddress,
                    onValueChange = viewModel::onPropertyAddressChange,
                    label = { Text(stringResource(R.string.property_address)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = viewModel::saveCalculation,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        enabled = uiState.isValid && uiState.propertyAddress.isNotBlank() && !uiState.isSaved
                    ) {
                        Text(stringResource(R.string.save))
                    }

                    OutlinedButton(
                        onClick = viewModel::reset,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(stringResource(R.string.reset))
                    }
                }
            }
        }
    }
}

@Composable
fun DSCRResultCard(
    ratio: Double,
    status: DSCRStatus,
    isActive: Boolean
) {
    val statusColor = status.statusColor()
    val gaugeColor = if (isActive) {
        statusColor
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }
    val trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
    val animatedRatio by animateFloatAsState(
        targetValue = if (isActive) ratio.toFloat() else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "ratioAnim"
    )
    val progress = (animatedRatio / 2f).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.dscr_ratio_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = trackColor,
                    strokeWidth = 8.dp,
                    trackColor = Color.Transparent,
                    strokeCap = StrokeCap.Round
                )

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = gaugeColor,
                    strokeWidth = 8.dp,
                    trackColor = Color.Transparent,
                    strokeCap = StrokeCap.Round
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format(Locale.getDefault(), "%.2f", animatedRatio),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = gaugeColor
                    )
                    if (isActive) {
                        Text(
                            text = stringResource(status.statusTextRes()),
                            color = gaugeColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    val isNearLimit = (value.toDoubleOrNull() ?: 0.0) > 900_000
    val supporting: (@Composable () -> Unit)? = if (isNearLimit) {
        {
            Text(
                text = stringResource(R.string.input_range_hint),
                style = MaterialTheme.typography.labelSmall
            )
        }
    } else {
        null
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        prefix = {
            if (value.isNotEmpty()) {
                Text("$", fontWeight = FontWeight.Bold)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        supportingText = supporting
    )
}
