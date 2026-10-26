package com.example.todo.ui.categories

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.block
import com.example.test.check
import com.example.todo.models.Category
import com.example.todo.models.CategoryColor
import com.example.todo.models.CategoryIcon
import com.example.todo.models.utils.toColor
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategorySheet(
    state: CategoryEditorState,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
           onDismiss()
        }, sheetState = sheetState, dragHandle = null, modifier = modifier
    ) {
        EditCategorySheetContent(
            state, onSave = onSave, modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun EditCategorySheetContent(
    state: CategoryEditorState, onSave: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Edit category", modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onSave) {
                Icon(check, null)
            }
        }
        OutlinedTextField(
            state.name, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Color", style = MaterialTheme.typography.titleSmall
        )
        FlowRow {
            CategoryColor.entries.sortedBy { it != CategoryColor.GRAY }.forEach {
                ColorOption(
                    color = it,
                    selected = it == state.color,
                    onSelect = { state.updateColor(it) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Icons", style = MaterialTheme.typography.titleSmall
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                block,
                null,
                tint = if (state.icon == null) state.color.toColor() else Color.LightGray,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        state.updateIcon(null)
                    })
            CategoryIcon.entries.forEach {
                Icon(
                    it.icon,
                    null,
                    tint = if (state.icon == it) state.color.toColor() else Color.LightGray,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            state.updateIcon(it)
                        })
            }
        }
    }
}

@Preview(uiMode = AndroidUiModes.UI_MODE_NIGHT_YES)
@Composable
private fun EditCategorySheetContentPrev() {
    AppTheme {
        EditCategorySheetContent(
            CategoryEditorState.fromCategory(
                Category(
                    id = 1,
                    name = "Work",
                    color = CategoryColor.RED,
                    icon = CategoryIcon.Work,
                    position = 1
                )
            ), onSave = {}, modifier = Modifier.padding(16.dp)
        )
    }
}