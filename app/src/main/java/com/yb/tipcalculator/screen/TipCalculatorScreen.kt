package com.yb.tipcalculator.screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radiusagent.brokerage.presentation.ui.theme.inter
import com.radiusagent.brokerage.presentation.ui.theme.montserrat
import com.yb.tipcalculator.screen.viewmodel.TipCalculatorViewModel
import com.yb.tipcalculator.utils.EndSuffixTransformation
import com.yb.tipcalculator.utils.ErrorSnackBar
import com.yb.tipcalculator.utils.commaFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author BHATTJI
 * Created 25-05-2024 at 09:41 pm
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorScreen(
    context: Context, viewModel: TipCalculatorViewModel
) {

    val uiState by viewModel.state.collectAsState()
    var showError by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = showError) {
        if (showError){
            delay(5000)
            showError = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tip Calculator",
                        fontSize = 24.sp,
                        fontFamily = inter,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontWeight = FontWeight.Black,
                            lineHeight = 30.sp
                        )
                    )
                },
            )
        },
    ) { _ ->

        LazyColumn(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()

        ) {

            item {
                Spacer(modifier = Modifier.padding(48.dp))

                OutlinedTextField(
                    label = {
                        Text(text = "Bill Amount", color = MaterialTheme.colorScheme.primary)
                    },
                    value = uiState.totalBillAmount,
                    onValueChange = {
                        viewModel.updateBillAmount(amount = it)
                    },
                    visualTransformation = { commaFilter(it) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                AnimatedVisibility(
                    visible = uiState.totalBillAmount.isNotEmpty() && uiState.totalBillAmount.toInt() > 0,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        OutlinedTextField(
                            label = {
                                Text(
                                    text = "Tip Percentage",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            value = uiState.tipPercentage,
                            visualTransformation = EndSuffixTransformation(" %"),
                            onValueChange = {
                                if (it.isNotEmpty() && it.toInt() > 100) {
                                    showError = true
                                }
                                viewModel.updateTipPercentage(tip = it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AnimatedVisibility(
                            visible = showError, enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            ErrorSnackBar(
                                showError,
                                message = "Sorry, But you can not give more tip than your bill :("
                            )

                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Slide to Increase/Decrease for Tip!",
                            color = MaterialTheme.colorScheme.primary,
                            style = TextStyle(
                                fontSize = 14.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = if (uiState.tipPercentage.isNotEmpty() && uiState.tipPercentage.toFloat() >= 0f) uiState.tipPercentage.toFloat() else 0f,
                            onValueChange = { viewModel.updateTipPercentage(it.toString()) },
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.secondary,
                                activeTrackColor = MaterialTheme.colorScheme.secondary,
                                inactiveTrackColor = MaterialTheme.colorScheme.secondary,
                            ),
                            steps = 100,
                            valueRange = 0f..100f
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = uiState.finalBill.isNotEmpty() && uiState.finalBill.toInt() > 0,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        OutlinedTextField(
                            label = {
                                Text(
                                    text = "Final Bill Including Tip",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            enabled = false,
                            value = uiState.finalBill,
                            onValueChange = {
                                viewModel.updateTipPercentage(tip = it)
                            },
                            visualTransformation = { commaFilter(it) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Wanna Split the bill?",
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = montserrat
                            ),
                            modifier = Modifier.clickable {
                                viewModel.showOrHideSplitOption()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = uiState.finalBill.isNotEmpty() && uiState.finalBill.toInt() > 0 && uiState.showSplitOption,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        OutlinedTextField(
                            label = {
                                Text(
                                    text = "Number Of People",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            visualTransformation = { commaFilter(it) },
                            value = uiState.splitNumber,
                            onValueChange = {
                                viewModel.updateSplit(number = it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AnimatedVisibility(
                            visible = uiState.splitAmount.isNotEmpty() && uiState.splitAmount.toInt() > 0,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Text(
                                text = "Hurrayyy!! It's ${uiState.splitAmount} bucks only :)",
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = montserrat
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}