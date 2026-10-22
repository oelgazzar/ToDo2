package com.example.todo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.close
import com.example.todo.ui.theme.AppTheme

@Composable
fun FieldChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDismissible: Boolean = true,
    onDismiss: () -> Unit = {},
    icon: ImageVector? = null
) {
    InputChip(
        false,
        onClick = onClick,
        label = {
            Text(
                label,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        },
        leadingIcon = {
            icon?.let {
                Icon(icon, null)
            }
        },
        trailingIcon = {
            if (isDismissible) {
                Icon(
                    close, null,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onDismiss)
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun DismissibleFieldChip() {
    AppTheme {
        FieldChip(
            label = "Repeat",
            onClick = {},
            isDismissible = true,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun NonDismissibleFieldChip() {
    AppTheme {
        FieldChip(
            label = "Repeat",
            onClick = {},
            isDismissible = false,
            onDismiss = {}
        )
    }
}