package com.epico.calculadoradepropina.ui.tip

enum class Rounding { EXACT, UP }

/** Resultado ya formateado por el ViewModel: la UI solo lo muestra. */
data class TipResult(
    val tip: String,
    val total: String,
    val perPerson: String
)

data class HistoryItem(
    val id: Int,
    val perPerson: String,
    val people: Int,
    val tipPercent: Int
)

/** Estado inmutable de la pantalla. result == null -> estado inicial; != null -> calculado. */
data class TipUiState(
    val billInput: String = "",
    val tipPercent: Int = 15,
    val people: Int = 1,
    val rounding: Rounding = Rounding.EXACT,
    val isBillError: Boolean = false,
    val result: TipResult? = null,
    val history: List<HistoryItem> = emptyList()
)