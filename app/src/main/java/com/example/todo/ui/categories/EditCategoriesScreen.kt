package com.example.todo.ui.categories

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.add
import com.example.test.arrow_back
import com.example.todo.data.categories
import com.example.todo.models.Category
import com.example.todo.ui.theme.AppTheme
import kotlinx.serialization.Serializable

@Serializable
object EditCategories

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditCategoriesScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: EditCategoriesViewModel = hiltViewModel()
) {
    val editorState by viewModel.editorState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Categories")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(arrow_back, null)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.startAdd() }) {
                        Icon(add, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        EditCategoriesContent(
            categories = categories,
            onEdit = viewModel::startEdit,
            onDelete = viewModel::deleteCategory,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }

    editorState?.let { state ->
        EditCategorySheet(
            state,
            onSave = viewModel::onSave,
            onDismiss = viewModel::onDismiss
        )
    }
}

@Composable
fun EditCategoriesContent(
    categories: List<Category>,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                CategoryRow(
                    category,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (category != categories.last()) {
                    HorizontalDivider(
                        modifier = Modifier
                            .alpha(.35f)
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditCategoriesPrev() {
    AppTheme {
        EditCategoriesContent(
            categories = categories,
            onEdit = {},
            onDelete = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

