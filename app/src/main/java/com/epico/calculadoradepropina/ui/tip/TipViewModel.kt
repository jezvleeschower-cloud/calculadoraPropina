package com.epico.calculadoradepropina.ui.tip

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import kotlin.math.ceil

class TipViewModel : ViewModel() {

    // Backing Property: la UI solo puede leer, nunca escribir.
    private val _uiState = MutableStateFlow(TipUiState())
    val uiState: StateFlow<TipUiState> = _uiState.asStateFlow()

    private val currencyFormat = NumberFormat.getCurrencyInstance()
    private var nextId = 0

    fun onBillChange(input: String) {
        val filtered = input.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } > 1) return
        updateAndCalculate { it.copy(billInput = filtered) }
    }

    fun onTipChange(percent: Int) {
        updateAndCalculate { it.copy(tipPercent = percent) }
    }

    fun onPeopleChange(delta: Int) {
        updateAndCalculate {
            it.copy(people = (it.people + delta).coerceIn(MIN_PEOPLE, MAX_PEOPLE))
        }
    }

    fun onRoundingChange(rounding: Rounding) {
        updateAndCalculate { it.copy(rounding = rounding) }
    }

    fun onSave() {
        val state = _uiState.value
        val result = state.result ?: return
        val item = HistoryItem(nextId++, result.perPerson, state.people, state.tipPercent)
        _uiState.update { it.copy(history = (listOf(item) + it.history).take(MAX_HISTORY)) }
    }

    fun onClearHistory() {
        _uiState.update { it.copy(history = emptyList()) }
    }

    private fun updateAndCalculate(transform: (TipUiState) -> TipUiState) {
        _uiState.update { calculate(transform(it)) }
    }

    private fun calculate(state: TipUiState): TipUiState {
        val bill = state.billInput.toDoubleOrNull()
        if (bill == null || bill <= 0.0) {
            return state.copy(isBillError = state.billInput.isNotEmpty(), result = null)
        }
        val rawPerPerson = bill * (1 + state.tipPercent / 100.0) / state.people
        val perPerson = if (state.rounding == Rounding.UP) ceil(rawPerPerson) else rawPerPerson
        val total = perPerson * state.people
        return state.copy(
            isBillError = false,
            result = TipResult(
                tip = currencyFormat.format(total - bill),
                total = currencyFormat.format(total),
                perPerson = currencyFormat.format(perPerson)
            )
        )
    }

    private companion object {
        const val MIN_PEOPLE = 1
        const val MAX_PEOPLE = 20
        const val MAX_HISTORY = 10
    }
}