package com.epico.calculadoradepropina.ui.tip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epico.calculadoradepropina.R
import com.epico.calculadoradepropina.ui.components.BillField
import com.epico.calculadoradepropina.ui.components.HistoryCard
import com.epico.calculadoradepropina.ui.components.PeopleSelector
import com.epico.calculadoradepropina.ui.components.ResultCard
import com.epico.calculadoradepropina.ui.components.RoundingChips
import com.epico.calculadoradepropina.ui.components.TipSlider

/** Único punto que conoce al ViewModel. */
@Composable
fun TipScreen(
    modifier: Modifier = Modifier,
    viewModel: TipViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TipContent(
        state = state,
        onBillChange = viewModel::onBillChange,
        onTipChange = viewModel::onTipChange,
        onPeopleChange = viewModel::onPeopleChange,
        onRoundingChange = viewModel::onRoundingChange,
        onSave = viewModel::onSave,
        onClearHistory = viewModel::onClearHistory,
        modifier = modifier
    )
}

/** Stateless: recibe estado inmutable y notifica eventos mediante lambdas. */
@Composable
fun TipContent(
    state: TipUiState,
    onBillChange: (String) -> Unit,
    onTipChange: (Int) -> Unit,
    onPeopleChange: (Int) -> Unit,
    onRoundingChange: (Rounding) -> Unit,
    onSave: () -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item { BillField(state.billInput, state.isBillError, onBillChange) }
        item { TipSlider(state.tipPercent, onTipChange) }
        item { PeopleSelector(state.people, onPeopleChange) }
        item { RoundingChips(state.rounding, onRoundingChange) }
        item {
            val result = state.result
            if (result == null) {
                Text(
                    text = stringResource(R.string.result_hint),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                ResultCard(result, onSave)
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.history_title),
                    style = MaterialTheme.typography.titleLarge
                )
                if (state.history.isNotEmpty()) {
                    TextButton(onClick = onClearHistory) {
                        Text(stringResource(R.string.history_clear))
                    }
                }
            }
        }
        if (state.history.isEmpty()) {
            item { Text(stringResource(R.string.history_empty)) }
        } else {
            items(state.history, key = { it.id }) { HistoryCard(it) }
        }
    }
}