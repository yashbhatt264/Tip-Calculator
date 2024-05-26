package com.yb.tipcalculator.screen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yb.tipcalculator.screen.TipCalculatorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author BHATTJI
 * Created 25-05-2024 at 09:49 pm
 */
@HiltViewModel
class TipCalculatorViewModel @Inject constructor(
    private val application: Application
)  : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TipCalculatorState())
    var state = _state.asStateFlow()


    fun updateBillAmount(amount: String){
        _state.update {
            it.copy(
                totalBillAmount = amount
            )
        }
    }

    fun updateTipPercentage(tip: String){
        _state.update {
            it.copy(
                tipPercentage = tip,
                finalBill = (if (it.totalBillAmount.isNotEmpty() && tip.isNotEmpty() && tip.toInt() > 0) ((it.totalBillAmount.toDouble() / 100 ) * tip.toDouble()).plus(it.totalBillAmount.toInt()).roundToInt() else 0).toString()
            )
        }
    }

    fun showOrHideSplitOption(){
        _state.update {
            it.copy(
                showSplitOption = !it.showSplitOption
            )
        }
    }

    fun updateSplit(number: String){
        _state.update {
            it.copy(
                splitNumber = number,
                splitAmount = if (number.isNotEmpty() && number.toInt() > 0) (it.finalBill.toDouble() / number.toDouble()).roundToInt().toString() else ""
            )
        }
    }
}