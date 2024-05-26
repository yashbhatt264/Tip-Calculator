package com.yb.tipcalculator.screen

/**
 * @author BHATTJI
 * Created 25-05-2024 at 03:05 pm
 */
data class TipCalculatorState(
 val totalBillAmount: String = "",
 val tipPercentage: String = "",
 val finalBill: String = "",
 val splitNumber: String = "",
 val tipSlider: Float = 0f,
 val showSplitOption: Boolean = false,
 val splitAmount:String = ""
)