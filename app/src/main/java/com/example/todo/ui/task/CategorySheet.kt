package com.example.todo.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.add
import com.example.test.check
import com.example.todo.models.Category
import com.example.todo.models.CategoryColor
import com.example.todo.models.CategoryIcon
import com.example.todo.models.utils.toColor
import com.example.todo.ui.components.ColorDot
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySheet(
    categories: List<Category>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,          // null = no category
    onCreate: (String) -> Unit,         // ViewModel creates + selects
    onEdit: () -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null
    ) {
        CategorySheetContent(
            categories = categories,
            selectedId = selectedId,
            onSelect = onSelect,
            onCreate = onCreate,
            onEdit = {
                coroutineScope.launch {
                    sheetState.hide()
                    onEdit()
                }
            },
            onDismiss = onDismiss
        )
    }
}

@Composable
fun CategorySheetContent(
    categories: List<Category>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,          // null = no category
    onCreate: (String) -> Unit,         // ViewModel creates + selects
    onEdit: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initiallyAdding: Boolean = false
) {
    var name by remember { mutableStateOf("") }

    val trimmed = name.trim()
    val duplicate = categories.firstOrNull { it.name.equals(trimmed, ignoreCase = true) }

    fun submit() {
        when {
            trimmed.isEmpty() -> return
            duplicate != null -> onSelect(duplicate.id)   // just select existing
            else -> onCreate(trimmed)
        }
        onDismiss()
    }

    var adding by remember { mutableStateOf(initiallyAdding) }
    val focus = remember { FocusRequester() }

    Column(
        modifier
            .padding(16.dp)
            .padding(bottom = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Category", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onEdit) {
                Text("Edit")
            }
        }
        Spacer(Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedId == null,
                onClick = { onSelect(null); onDismiss() },
                label = { Text("None") },
            )
            categories.forEach { c ->
                FilterChip(
                    selected = c.id == selectedId,
                    onClick = { onSelect(c.id); onDismiss() },
                    label = { Text(c.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    leadingIcon = {
                        c.icon?.let {
                            Icon(c.icon.icon, null, tint = c.color?.toColor() ?: Color.Unspecified)
                        } ?: c.color?.let {
                            ColorDot(
                                c.color.toColor()
                            )
                        }
                    }
                )
            }
            if (!adding) {
                AssistChip(
                    onClick = { adding = true },
                    label = { Text("New") },
                    leadingIcon = { Icon(add, null) }
                )
            }
        }

        if (adding) {
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { if (it.length <= 24) name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus),
                singleLine = true,
                label = { Text("New category") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                trailingIcon = {
                    IconButton(onClick = ::submit, enabled = trimmed.isNotEmpty()) {
                        Icon(check, "Add")
                    }
                }
            )
            LaunchedEffect(Unit) { focus.requestFocus() }
        }
    }
}

@Preview
@Composable
private fun CategorySheetPrev() {
    AppTheme {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CategorySheetContent(
                listOf(
                    Category(
                        id = 1,
                        name = "Work",
                        color = CategoryColor.RED,
                        icon = CategoryIcon.Work,
                        position = 1
                    ),
                    Category(
                        id = 2,
                        name = "Shopping",
                        color = CategoryColor.AMBER,
                        icon = CategoryIcon.Shopping,
                        position = 1
                    )
                ),
                selectedId = 1,
                onSelect = {},
                onCreate = {},
                onEdit = {},
                onDismiss = {},
            )
        }
    }
}

@Preview
@Composable
private fun CategorySheetAddingPrev() {
    AppTheme {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CategorySheetContent(
                listOf(
                    Category(
                        id = 1,
                        name = "Work",
                        color = CategoryColor.BROWN,
                        icon = CategoryIcon.Work,
                        position = 1
                    ),
                    Category(
                        id = 2,
                        name = "Shopping",
                        color = CategoryColor.GREEN,
                        icon = CategoryIcon.Shopping,
                        position = 1
                    )
                ),
                selectedId = 1,
                onSelect = {},
                onCreate = {},
                onEdit = {},
                onDismiss = {},
                initiallyAdding = true
            )
        }
    }
}