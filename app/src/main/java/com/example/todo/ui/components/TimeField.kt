package com.example.todo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.test.close
import java.time.LocalTime

@Composable
fun TimeField(
    time: LocalTime?,
    onTimeChange: (LocalTime?) -> Unit,
    modifier: Modifier = Modifier
) {

    var showTimePickerDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = time?.toString() ?: "Set time",
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            Icon(
                close,
                null,
                modifier = Modifier.clickable(onClick = { onTimeChange(null) })
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(pass = PointerEventPass.Initial)
                    val trailingIconWidth = 48.dp.toPx()

                    val isTrailingIcon = down.position.x >=
                            size.width - trailingIconWidth

                    if (!isTrailingIcon) {
                        val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (up != null) {
                            up.consume()
                            showTimePickerDialog = true
                        }
                    }
                }
            }
    )

    if (showTimePickerDialog) {
        TimePickerDialog(
            initialTime = time,
            onConfirm = onTimeChange,
            onDismiss = { showTimePickerDialog = false }
        )
    }
}