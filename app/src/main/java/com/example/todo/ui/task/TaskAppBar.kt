package com.example.todo.ui.task

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.test.arrow_back
import com.example.test.more_vert
import com.example.test.star
import com.example.test.star_filled
import com.example.todo.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(
    isStarred: Boolean,
    toggleStarred: () -> Unit,
    navigateBack: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(arrow_back, null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        actions = {
            IconButton(
                onClick = toggleStarred,
            ) {
                Icon(if (isStarred) star_filled else star, null)
            }
            Box() {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.testTag("menu_button")
                ) {
                    Icon(more_vert, null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false}
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Delete")
                        },
                        onClick = {
                            onDelete()
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun TaskAppBarPrev() {
    AppTheme {
        TaskAppBar(
            isStarred = true,
            toggleStarred = {},
            onDelete = {},
            navigateBack = {}
        )
    }
}