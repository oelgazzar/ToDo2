package com.example.todo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    values: List<T>,
    currentValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    // For plural s
    quantity: Int = 1
) {
    val pluralS by remember(quantity) { derivedStateOf { if (quantity > 1) "s" else "" } }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = true },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = currentValue.toString() + pluralS,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            values.forEach {
                DropdownMenuItem(
                    text = { Text(it.toString() + pluralS) },
                    onClick = {
                        onValueChange(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropdownSelectorPrev() {
    AppTheme {
        DropdownSelector(
            values = listOf("One", "Two", "Three"),
            currentValue = "One",
            onValueChange = {},
            quantity = 2,
            modifier = Modifier.fillMaxWidth()
        )
    }
}