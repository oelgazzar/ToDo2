package com.example.todo.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.test.check
import com.example.test.swap_vert
import com.example.todo.models.SortType

@Composable
fun SortingMenu(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    selectedSortType: SortType,
    onSortChanged: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = onExpand
        ) {
            Icon(swap_vert, null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            DropdownMenuItem(
                text = {
                    Text("My order")
                },
                onClick = {
                    onSortChanged(SortType.CUSTOM)
                    onDismiss()
                },
                leadingIcon =
                    {
                        if (selectedSortType == SortType.CUSTOM) {
                            Icon(check, null)
                        }
                    }
            )
            DropdownMenuItem(
                text = {
                    Text("Deadline")
                },
                onClick = {
                    onSortChanged(SortType.DEADLINE)
                    onDismiss()
                },
                leadingIcon =
                    {
                        if (selectedSortType == SortType.DEADLINE) {
                            Icon(check, null)
                        }
                    }
            )
            DropdownMenuItem(
                text = {
                    Text("Reminder")
                },
                onClick = {
                    onSortChanged(SortType.REMINDER)
                    onDismiss()
                },
                leadingIcon =
                    {
                        if (selectedSortType == SortType.REMINDER) {
                            Icon(check, null)
                        }
                    }
            )
            DropdownMenuItem(
                text = {
                    Text("Title")
                },
                onClick = {
                    onSortChanged(SortType.TITLE)
                    onDismiss()
                },
                leadingIcon =
                    {
                        if (selectedSortType == SortType.TITLE) {
                            Icon(check, null)
                        }
                    }
            )
        }
    }
}