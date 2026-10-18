@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.list_2
import com.example.todo.models.Task
import com.example.todo.ui.components.TransparentTextField
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun TaskFormSheet(
    onDismiss: () -> Unit,
    onAddTask: (Task) -> Unit,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val titleFieldState = rememberTextFieldState("")
    val detailsFieldState = rememberTextFieldState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showDetailsField by remember { mutableStateOf(false) }

    val titleFocusRequester = remember { FocusRequester() }
    val detailsFocusRequester = remember { FocusRequester() }

    LaunchedEffect(sheetState.isVisible, showDetailsField) {
        if (sheetState.isVisible) {
            titleFieldState.setTextAndPlaceCursorAtEnd(titleFieldState.text.toString())
            titleFocusRequester.requestFocus()
        }

        if (showDetailsField) {
            detailsFocusRequester.requestFocus()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                onDismiss()
            }
        },
        dragHandle = {},
        sheetState =  sheetState,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp)
                .wrapContentHeight()
        ) {
            TransparentTextField(
                state = titleFieldState,
                placeholderText = "New Task",
                focusRequester = titleFocusRequester,
            )

            if (showDetailsField) {
                TransparentTextField(
                    state = detailsFieldState,
                    placeholderText = "Add details",
                    style = MaterialTheme.typography.bodyMedium,
                    focusRequester = detailsFocusRequester,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                /* Add Details Button */
                IconButton(
                    onClick = {
                        showDetailsField = true
                    },
                ) {
                    Icon(
                        list_2,
                        null,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                /* Save Button */
                TextButton(
                    onClick = {
                        onAddTask(Task(
                            title = titleFieldState.text.toString(),
                            details = detailsFieldState.text.toString().takeIf { it.isNotBlank() }
                        ))
                        onDismiss()
                    },
                    enabled = titleFieldState.text.isNotBlank(),
                ) {
                    Text("Save")
                }
            }

        }
    }
}

@Preview
@Composable
private fun TaskFormSheetPrev() {
    AppTheme {
        TaskFormSheet(
            onDismiss = {},
            onAddTask = {},
            modifier = Modifier
        )
    }
}