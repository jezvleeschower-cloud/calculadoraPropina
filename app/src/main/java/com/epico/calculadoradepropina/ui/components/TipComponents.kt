package com.epico.calculadoradepropina.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.epico.calculadoradepropina.R
import com.epico.calculadoradepropina.ui.tip.HistoryItem
import com.epico.calculadoradepropina.ui.tip.Rounding
import com.epico.calculadoradepropina.ui.tip.TipResult
import kotlin.math.roundToInt

private const val MAX_TIP_PERCENT = 30
private val RESULT_PADDING = 16.dp

@Composable
fun BillField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.bill_label)) },
        isError = isError,
        supportingText = { if (isError) Text(stringResource(R.string.bill_error)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true
    )
}

@Composable
fun TipSlider(
    percent: Int,
    onPercentChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.tip_label, percent),
            style = MaterialTheme.typography.titleMedium
        )
        Slider(
            value = percent.toFloat(),
            onValueChange = { onPercentChange(it.roundToInt()) },
            valueRange = 0f..MAX_TIP_PERCENT.toFloat(),
            steps = MAX_TIP_PERCENT - 1
        )
    }
}

@Composable
fun PeopleSelector(
    people: Int,
    onPeopleChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.people_label),
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = { onPeopleChange(-1) }) {
                Text(stringResource(R.string.people_decrease))
            }
            Text(text = people.toString(), style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { onPeopleChange(1) }) {
                Text(stringResource(R.string.people_increase))
            }
        }
    }
}

@Composable
fun RoundingChips(
    selected: Rounding,
    onSelect: (Rounding) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Rounding.entries.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onSelect(option) },
                label = { Text(stringResource(option.labelRes())) }
            )
        }
    }
}

@StringRes
private fun Rounding.labelRes(): Int = when (this) {
    Rounding.EXACT -> R.string.rounding_exact
    Rounding.UP -> R.string.rounding_up
}

@Composable
fun ResultCard(
    result: TipResult,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(RESULT_PADDING),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(stringResource(R.string.result_tip), result.tip)
            ResultRow(stringResource(R.string.result_total), result.total)
            ResultRow(
                label = stringResource(R.string.result_per_person),
                value = result.perPerson,
                highlighted = true
            )
            Button(onClick = onSave, modifier = Modifier.align(Alignment.End)) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, highlighted: Boolean = false) {
    val style = if (highlighted) {
        MaterialTheme.typography.headlineSmall
    } else {
        MaterialTheme.typography.bodyLarge
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = style)
        Text(text = value, style = style)
    }
}

@Composable
fun HistoryCard(item: HistoryItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = stringResource(
                R.string.history_item,
                item.perPerson,
                item.people,
                item.tipPercent
            ),
            modifier = Modifier.padding(RESULT_PADDING),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}